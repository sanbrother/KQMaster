package com.neosoft.assistant.kq;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

public class Base64Codec {

	public static String decrypt(String s) {
		String s1 = "";
		String s2 = "";
		String s3 = "";

		for (int i = 0; i < 26; i++) {
			s1 += String.format("%c", 'A' + i);
			s1 += String.format("%c", 'a' + i);
			s2 += String.format("%c", 'Z' - i);
			s2 += String.format("%c", 'z' - i);
		}

		s1 += "0123456789";
		s2 += "8967452301";

		for (char c : s.toCharArray()) {
			int index = s2.indexOf(c);

			if (-1 != index) {
				s3 += String.format("%c", s1.charAt(index));
			} else {
				s3 += String.format("%c", c);
			}
		}

		return decode(s3);
	}

	public static String encrypt(String text) {
		String s1 = "";
		String s2 = "";
		String s3 = "";
		String s = encode(text);

		for (int i = 0; i < 26; i++) {
			s1 += String.format("%c", 'A' + i);
			s1 += String.format("%c", 'a' + i);
			s2 += String.format("%c", 'Z' - i);
			s2 += String.format("%c", 'z' - i);
		}

		s1 += "0123456789";
		s2 += "8967452301";

		for (char c : s.toCharArray()) {
			int index = s1.indexOf(c);

			if (-1 != index) {
				s3 += String.format("%c", s2.charAt(index));
			} else {
				s3 += String.format("%c", c);
			}
		}

		return s3;
	}

	public static String decode(String s) {
		byte[] bytes = Base64.decode(s, Base64.NO_WRAP | Base64.URL_SAFE);

		String decoded = null;
		try {
			decoded = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return decoded;
	}

	public static String encode(String s) {
		byte[] data = null;
		try {
			data = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		return Base64.encodeToString(data, Base64.NO_WRAP | Base64.URL_SAFE);
	}

}
