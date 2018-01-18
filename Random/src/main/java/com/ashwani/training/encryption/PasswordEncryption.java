package com.ashwani.training.encryption;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.registry.AlgorithmRegistry;
import org.jasypt.util.text.BasicTextEncryptor;

public class PasswordEncryption {

	public static void main(String[] args) {
		for (final Object object : AlgorithmRegistry.getAllPBEAlgorithms()) {
			System.out.println(object.toString());
		}

		final String text = "SWYxewwK/WQcAq6OacEeyPFpdRtAlhrI";
		final BasicTextEncryptor encrypter = new BasicTextEncryptor();
		encrypter.setPassword("adpDbCryptKey#123");
		final String decrypt = encrypter.decrypt(text);
		System.out.println("Decrypted pwd: " + decrypt);

		final StandardPBEStringEncryptor standardEncryptor = new StandardPBEStringEncryptor();
		standardEncryptor.setAlgorithm("PBEWITHHMACSHA224ANDAES_256");
		standardEncryptor.setPassword("adpDbCryptKey#123");
		final String encryptedText = standardEncryptor.encrypt("Hello");
		System.out.println("Encrypted text: " + encryptedText);

		System.out.println("Decypted text: " + standardEncryptor.decrypt(encryptedText));

	}
}
