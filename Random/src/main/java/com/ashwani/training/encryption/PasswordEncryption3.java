package com.ashwani.training.encryption;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordEncryption3 {

	public static void main(String[] args) {
		final StandardPBEStringEncryptor myFirstEncryptor = new StandardPBEStringEncryptor();
		myFirstEncryptor.setProvider(new BouncyCastleProvider());
		myFirstEncryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		myFirstEncryptor.setPassword("hello");
	}
}