package com.tibco.ma.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodecUtil {
	public static String genMD5Token(String client_secret, String client_id,
			String request_time, String client_agent)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();

		messageDigest
				.update((client_secret + client_id + request_time + client_agent)
						.getBytes("UTF-8"));
		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();

	}

	public static String genMD5Token(String app_key, String rest_api_key,
			String client_secret, String client_id, String request_time,
			String client_agent) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {

		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();

		messageDigest.update((app_key + rest_api_key + client_secret
				+ client_id + request_time + client_agent).getBytes("UTF-8"));
		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();

	}

	public static String genPassword(String email, String id)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] bytesOfMessage = null;
		bytesOfMessage = email.getBytes("UTF-8");

		MessageDigest md = null;
		byte[] thedigest = null;
		md = MessageDigest.getInstance("SHA-256");
		thedigest = md.digest(bytesOfMessage);

		String salt = id;
		byte[] bytesOfMessageSalt = null;
		bytesOfMessageSalt = salt.getBytes("UTF-8");

		byte[] thedigestSalt = null;
		thedigestSalt = md.digest(bytesOfMessageSalt);

		// convert the byte to hex format
		StringBuffer emailSHAhashBuffer = new StringBuffer();
		for (int i = 0; i < thedigest.length; i++) {
			emailSHAhashBuffer.append(Integer.toHexString(0xFF & thedigest[i]));
		}

		// convert the byte to hex format
		StringBuffer saltSHAhashBuffer = new StringBuffer();
		for (int i = 0; i < thedigestSalt.length; i++) {
			saltSHAhashBuffer.append(Integer
					.toHexString(0xFF & thedigestSalt[i]));
		}

		String emailSHAhash = emailSHAhashBuffer.toString();
		String saltSHAhash = saltSHAhashBuffer.toString();

		String password = emailSHAhash + saltSHAhash;

		password = password.substring(56, 66);

		return password;
	}

	public static void main(String[] args) {
		try {

			String kigns_client_secret = "S2luZydzIGNsaWVudCBzZWNyZXQgYXQgMjAxMy0wOC0wNiAxNTo1NTowMA==";
			String client_id = "1fa2b759fc7a2cabc496b8f6af88b232";
			String request_time = "1372207293819";
			String client_agent = "Microsite";
			String token = genMD5Token(kigns_client_secret, client_id,
					request_time, client_agent);
			System.out.println(token);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
