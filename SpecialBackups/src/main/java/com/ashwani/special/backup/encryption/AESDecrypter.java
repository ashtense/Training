package com.ashwani.special.backup.encryption;

import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_ALGO;
import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_ALGO_CONFIG;
import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_KEY_BIT_SIZE;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_GENERATION_ALGORITHM;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_GENERATION_PASSWORD;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_ITERATION_COUNT;
import static com.ashwani.special.backup.encryption.AESConstants.SALT_LENGTH;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * AES-256 bit decryption utility. Based on password based key generation and a
 * known salting logic.
 * </p>
 */
public final class AESDecrypter {
	private static final Decoder DECODER = Base64.getDecoder();
	private static final String DECRYPTION_FAILURE = "Decryption Failure.";

	private AESDecrypter() {
	}

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
	 *            encrypted data
	 * @throws GeneralSecurityException
	 *             if the mechanics of decrypting the data fail for any reason
	 * @return decrypted data
	 */
	public static String decryptText(String textToDecrypt) throws GeneralSecurityException {
		try {
			final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO_CONFIG);
			// Extract saltBytes
			final ByteBuffer buffer = ByteBuffer.wrap(DECODER.decode(textToDecrypt));
			final byte[] saltBytes = new byte[SALT_LENGTH];
			buffer.get(saltBytes, 0, saltBytes.length);

			// Extract initialization Vector
			final byte[] initializationVector = new byte[cipher.getBlockSize()];
			buffer.get(initializationVector, 0, initializationVector.length);

			// text to decrypt after stripping salts and IVs
			final byte[] encryptedText = new byte[buffer.capacity() - saltBytes.length - initializationVector.length];
			buffer.get(encryptedText);

			// Deriving the key based on the extracted salt
			final SecretKeySpec secret = extractDecryptionKey(saltBytes);

			// Now we have secret key and IV which is enough for decryption.
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(initializationVector));

			return new String(cipher.doFinal(encryptedText));
		} catch (final Exception e) {
			throw new GeneralSecurityException(DECRYPTION_FAILURE, e);
		}
	}

	private static SecretKeySpec extractDecryptionKey(final byte... saltBytes) throws GeneralSecurityException {
		try {
			final SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
			final KeySpec spec = new PBEKeySpec(KEY_GENERATION_PASSWORD.toCharArray(), saltBytes, KEY_ITERATION_COUNT,
					ENCRYPTION_KEY_BIT_SIZE);
			final SecretKey secretKey = factory.generateSecret(spec);
			return new SecretKeySpec(secretKey.getEncoded(), ENCRYPTION_ALGO);
		} catch (final Exception e) {
			throw new GeneralSecurityException(DECRYPTION_FAILURE, e);
		}
	}

}
