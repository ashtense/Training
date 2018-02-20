package com.ashwani.encryption;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * AES-256 bit decryption utility. Based on password based key generation and a
 * known salting logic.
 * </p>
 *
 * @author solanka
 */
public class AESDecrypter {

	/**
	 * <p>
	 * 1.) First we decode the cipher from BASE-64 encoding and separate the salt
	 * bytes. <br>
	 * 2.) We derive our secret encryption key based on saltBytes and the known key
	 * generation password. <br>
	 * 3.) After the secret key generation we decrypt on the known algorithm and
	 * padding configuration.
	 * </p>
	 *
	 * @param textToDecrypt
	 * @return
	 */
	public String decryptText(String textToDecrypt) {
		String decryptedString = null;
		try {
			final Cipher cipher = Cipher.getInstance(AESConstants.ENCRYPTION_ALGO_CONFIG);
			// Extract saltBytes
			final ByteBuffer buffer = ByteBuffer.wrap(new Base64().decode(textToDecrypt));
			final byte[] saltBytes = new byte[AESConstants.SALT_LENGTH];
			buffer.get(saltBytes, 0, saltBytes.length);

			// Extract initialization Vector
			final byte[] initializationVectorBytes = new byte[cipher.getBlockSize()];
			buffer.get(initializationVectorBytes, 0, initializationVectorBytes.length);

			// text to decrypt after stripping salts and IVs
			final byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length
					- initializationVectorBytes.length];
			buffer.get(encryptedTextBytes);

			// Deriving the key based on the extracted salt
			final SecretKeySpec secret = extractDecryptionKey(saltBytes);

			// Now we have secret key and IV which is enough for decryption.
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(initializationVectorBytes));

			byte[] decryptedTextBytes = null;
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
			decryptedString = new String(decryptedTextBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
		}
		return decryptedString;
	}

	private SecretKeySpec extractDecryptionKey(final byte[] saltBytes) {
		SecretKeySpec secretKeySpec = null;
		try {
			final SecretKeyFactory factory = SecretKeyFactory.getInstance(AESConstants.KEY_GENERATION_ALGORITHM);
			final PBEKeySpec spec = new PBEKeySpec(AESConstants.KEY_GENERATION_PASSWORD.toCharArray(), saltBytes,
					AESConstants.KEY_ITERATION_COUNT, AESConstants.ENCRYPTION_KEY_BIT_SIZE);
			final SecretKey secretKey = factory.generateSecret(spec);
			secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AESConstants.ENCRYPTION_ALGO);
		} catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
		}
		return secretKeySpec;
	}

}
