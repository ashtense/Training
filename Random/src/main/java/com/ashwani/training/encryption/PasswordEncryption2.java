package com.ashwani.training.encryption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordEncryption2 {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
	/*	final String message = "Hello Geronimo";
		final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		final SecretKey secretKey = keyGenerator.generateKey();
		final byte[] rawSecretKey = secretKey.getEncoded();
		final SecretKeySpec secretKeySpec = new SecretKeySpec(rawSecretKey, "AES");
		final Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		final byte[] finalEncryption = cipher.doFinal(message.getBytes());
		System.out.println(finalEncryption);*/
		
		String message="Message to Decode";

		KeyGenerator key = KeyGenerator.getInstance("AES"); 
		key.init(128);

		SecretKey s = key.generateKey(); 
		byte[] raw = s.getEncoded();

		SecretKeySpec sskey= new SecretKeySpec(raw, "AES");

		Cipher c = Cipher.getInstance("AES");

		c.init(Cipher.ENCRYPT_MODE, s);

		byte[] encrypted = c.doFinal(message.getBytes()); 
		System.out.println(encrypted);


	}
}
