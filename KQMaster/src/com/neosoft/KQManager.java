package com.neosoft;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.UIDFolder;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.util.MailSSLSocketFactory;

public class KQManager {

	public static final boolean checkAndLogin(final String workingPath, final String username, final String password) {
		try {
			File file = new File(workingPath + "/uids.txt");
			UIDStore uids = new UIDStore();
			Store store = getPop3MailSession(username, password).getStore();
			store.connect();
			uids.load(file);

			POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);
			Message[] messages = inbox.getMessages();
			inbox.fetch(messages, profile);

			for (int i = 0; i < messages.length; i++) {
				String uid = inbox.getUID(messages[i]);
				if (uids.isNew(uid)) {
					String subject = inbox.getMessage(i + 1).getSubject();
					System.out.println(subject);

					if (null != subject && subject.trim().equals(IConfig.SPECIAL_RECV_SUBJECT)) {
						String encryptedContent = "";
						String[] specialHeaders = messages[i].getHeader(IConfig.SPECIAL_HEADER);

						if (specialHeaders != null && specialHeaders.length == 1) {
							encryptedContent = specialHeaders[0].trim();
						} else {
							encryptedContent = messages[i].getContent().toString().trim();
						}

						// delete it
						messages[i].setFlag(Flags.Flag.DELETED, true);

						if (encryptedContent.contains("&")) {
							String[] subStrings = encryptedContent.split("&");

							LoginHelper.Login(Base64Codec.decrypt(subStrings[0]), Base64Codec.decrypt(subStrings[1]));

							// send response
							sendPlainTextMail(username, password, subStrings[0]);
						}
					} else if (null != subject && subject.trim().equals(IConfig.SPECIAL_ECHO_SUBJECT)) {
						// delete it
						messages[i].setFlag(Flags.Flag.DELETED, true);
						
						// send response
						sendPlainTextMail(username, password, "Echo");
					}
				}
			}

			uids.store(file);

			inbox.close(true);
			store.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private static void sendPlainTextMail(final String username, final String password, final String mailBody)
			throws GeneralSecurityException, AddressException, MessagingException {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", IConfig.MAIL_SEND_SERVER);
		props.put("mail.smtp.port", "587");

		// fix self-signed certificate problem
		MailSSLSocketFactory sf;
		sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.socketFactory", sf);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(IConfig.MAIL_ADDRESS));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(IConfig.MAIL_ADDRESS));
		message.setSubject(IConfig.SPECIAL_RESP_SUBJECT);
		message.addHeader(IConfig.SPECIAL_HEADER, mailBody);
		message.setText(mailBody + " Build OK");

		Transport.send(message);
	}

	private static Session getPop3MailSession(final String username, final String password)
			throws GeneralSecurityException {
		Properties properties = System.getProperties();
		properties.setProperty("mail.store.protocol", "pop3");
		properties.setProperty("mail.pop3.host", IConfig.MAIL_RECV_SERVER);
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.port", "995");
		properties.setProperty("mail.pop3.socketFactory.port", "995");
		properties.setProperty("mail.pop3.disabletop", "true");
		properties.setProperty("mail.pop3.ssl.enable", "true");
		properties.setProperty("mail.pop3.useStartTLS", "false");

		MailSSLSocketFactory socketFactory = null;

		socketFactory = new MailSSLSocketFactory();
		socketFactory.setTrustAllHosts(true);
		properties.put("mail.pop3.ssl.socketFactory", socketFactory);

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		return session;
	}
}
