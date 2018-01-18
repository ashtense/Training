package com.ashwani.training.encryption;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AES2 {

	static String IV = "AAAAAAAAAAAAAAAA";
	static String plaintext = "test text 123\0\0\0"; /* Note null padding */

	public static void main(String[] args) {
		try {
			// Here we generate the key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(256);
			byte[] encryptionKey = keyGenerator.generateKey().getEncoded();
			System.out.print("Encryption key generated is: ");
			for (byte encryptionKeyByte : encryptionKey) {
				System.out.print(encryptionKeyByte);
			}
			System.out.println("");

			// Here we receive the cipher text
			System.out.println("Text to encrypt is: " + plaintext);
			byte[] cipher = encrypt(plaintext, encryptionKey);
			System.out.print("cipher:  ");
			for (int i = 0; i < cipher.length; i++)
				System.out.print(new Integer(cipher[i]) + " ");
			System.out.println("");

			//Here we decrypt the cipher
			String decrypted = decrypt(cipher, encryptionKey);
			System.out.println("decrypt: " + decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] encrypt(String plainText, byte[] encryptionKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(plainText.getBytes("UTF-8"));
	}

	public static String decrypt(byte[] cipherText, byte[] encryptionKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(cipherText), "UTF-8");
	}
}