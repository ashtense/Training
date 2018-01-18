package com.ashwani.encryption;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

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
 * AES-256 bit encryption utility. Based on password based key generation and a
 * known salting logic.
 * </p>
 *
 * @author solanka
 */
public class AESEncrypter {

	/**
	 * <p>
	 * 1.) First we generate salts and private encryption key for AES256 bit
	 * encryption.<br>
	 * 2.) Add the salts and Initialization Vector to the encrypted byte array to
	 * form the final encrypted text.
	 * </p>
	 *
	 * @param textToEncrypt
	 * @return
	 */
	public String encryptText(final String textToEncrypt) {
		String cipherString = null;
		final byte[] saltBytes = generateSaltBytes();
		final SecretKeySpec secretKeyGenerated = generateSecretKey(saltBytes);
		byte[] initializationVectorBytes;
		if (secretKeyGenerated != null) {
			try {
				final Cipher cipher = Cipher.getInstance(AESConstants.ENCRYPTION_ALGO_CONFIG);
				cipher.init(Cipher.ENCRYPT_MODE, secretKeyGenerated);
				final AlgorithmParameters params = cipher.getParameters();
				initializationVectorBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
				final byte[] encryptedTextBytes = cipher
						.doFinal(textToEncrypt.getBytes(AESConstants.ENCODING_STANDARD));

				// prepend salt and initializationVector
				final byte[] buffer = new byte[saltBytes.length + initializationVectorBytes.length
						+ encryptedTextBytes.length];
				System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
				System.arraycopy(initializationVectorBytes, 0, buffer, saltBytes.length,
						initializationVectorBytes.length);
				System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + initializationVectorBytes.length,
						encryptedTextBytes.length);
				cipherString = new Base64().encodeToString(buffer);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
					| InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				LOGGER.error("Exception occured during encryption/salting", e);
			}
		}
		return cipherString;
	}

	/**
	 * <p>
	 * We will generate secret password encryption key on the basis of a password
	 * and generated saltbytes.<br>
	 * Password based key along with salt will help in password generation for
	 * decryption of the cipher. Key generation using PBKDF2WithHmacSHA512: <br>
	 * Password Based Key Definition Function V2 with Hash Based Message
	 * Authentication Code and SHA512 hashing of the key.
	 * </p>
	 *
	 * @param saltBytes
	 * @return
	 */
	private SecretKeySpec generateSecretKey(final byte[] saltBytes) {
		SecretKeySpec secretKeySpec = null;
		// Derive the key
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance(AESConstants.KEY_GENERATION_ALGORITHM);
			final PBEKeySpec spec = new PBEKeySpec(AESConstants.KEY_GENERATION_PASSWORD.toCharArray(), saltBytes,
					AESConstants.KEY_ITERATION_COUNT, AESConstants.ENCRYPTION_KEY_BIT_SIZE);
			final SecretKey secretKey = factory.generateSecret(spec);
			final byte[] secretKeyStream = secretKey.getEncoded();
			secretKeySpec = new SecretKeySpec(secretKeyStream, AESConstants.ENCRYPTION_ALGO);
		} catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error("Exception occured while generating secretKey", e);
		}
		return secretKeySpec;
	}

	/**
	 * <p>
	 * These are random bytes fed into a one way function to modify the cipher,
	 * which protects the encryption against some textbook attacks, like rainbow
	 * table.
	 * </p>
	 *
	 * @return
	 */
	private byte[] generateSaltBytes() {
		final SecureRandom random = new SecureRandom();
		final byte bytes[] = new byte[AESConstants.SALT_LENGTH];
		random.nextBytes(bytes);
		return bytes;
	}
}
