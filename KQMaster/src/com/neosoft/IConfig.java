package com.neosoft;

public interface IConfig {
	final static String SPECIAL_ECHO_SUBJECT = "[Hudson][Echo]";
	final static String SPECIAL_RECV_SUBJECT = "[Hudson][Status]";
	final static String SPECIAL_RESP_SUBJECT = "[Hudson][Response]";
	final static String SPECIAL_HEADER  = "Fingerprint";
	
	final static String MAIL_RECV_SERVER = "mail.neusoft.com";
	final static String MAIL_SEND_SERVER = "smtp.neusoft.com";
	final static String MAIL_ADDRESS = "gaodw@neusoft.com";
	
	final static String BASE_URL = "http://kq.neusoft.com";
}
