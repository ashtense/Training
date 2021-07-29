package com.ashwani.special.backup.encryption;

import static com.ashwani.special.backup.encryption.AESConstants.ENCODING_STANDARD;
import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_ALGO;
import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_ALGO_CONFIG;
import static com.ashwani.special.backup.encryption.AESConstants.ENCRYPTION_KEY_BIT_SIZE;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_GENERATION_ALGORITHM;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_GENERATION_PASSWORD;
import static com.ashwani.special.backup.encryption.AESConstants.KEY_ITERATION_COUNT;
import static com.ashwani.special.backup.encryption.AESConstants.SALT_LENGTH;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-256 bit encryption utility. Based on password based key generation and a
 * known salting logic.
 */
public final class AESEncrypter {
	private static final Encoder ENCODER = Base64.getEncoder();
	private static final String LINE_SEPERATOR = "-----%s ENCRYPTED TEXT-----" + System.lineSeparator();
	private static final String ENCRYPTION_FAILURE = "Encryption Failure.";

	private AESEncrypter() {
	}

	/**
	 * <p>
	 * 1.) First we generate salts and private encryption key for AES256 bit
	 * encryption.<br>
	 * 2.) Add the salts and Initialization Vector to the encrypted byte array to
	 * form the final encrypted text.
	 * </p>
	 *
	 * @param textToEncrypt
	 *            String to encrypt
	 * @return encrypted String
	 */
	public static String encryptText(final String textToEncrypt) throws GeneralSecurityException {
		final byte[] saltBytes = generateSaltBytes();
		final SecretKeySpec secretKeyGenerated = generateSecretKey(saltBytes);
		try {
			final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO_CONFIG);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeyGenerated);
			final AlgorithmParameters params = cipher.getParameters();
			final byte[] initializationVector = params.getParameterSpec(IvParameterSpec.class).getIV();
			final byte[] encryptedTextBytes = cipher.doFinal(textToEncrypt.getBytes(ENCODING_STANDARD));

			// prepend salt and initializationVector
			final byte[] buffer = new byte[saltBytes.length + initializationVector.length + encryptedTextBytes.length];
			System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
			System.arraycopy(initializationVector, 0, buffer, saltBytes.length, initializationVector.length);
			System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + initializationVector.length,
					encryptedTextBytes.length);
			return ENCODER.encodeToString(buffer);
		} catch (final Exception e) {
			throw new GeneralSecurityException(ENCRYPTION_FAILURE, e);
		}

	}

	public static void main(String... args) throws Exception {
		if ((args == null) || (args.length != 1)) {
			throw new IllegalArgumentException("Requires single argument containing test to encrypt");
		}
		System.out.printf(LINE_SEPERATOR, "BEGIN");
		System.out.println(encryptText(args[0]));
		System.out.printf(LINE_SEPERATOR, "END");
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
	private static SecretKeySpec generateSecretKey(final byte... saltBytes) throws GeneralSecurityException {
		// Derive the key
		try {
			final SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
			final PBEKeySpec spec = new PBEKeySpec(KEY_GENERATION_PASSWORD.toCharArray(), saltBytes,
					KEY_ITERATION_COUNT, ENCRYPTION_KEY_BIT_SIZE);
			final SecretKey secretKey = factory.generateSecret(spec);
			final byte[] secretKeyStream = secretKey.getEncoded();
			return new SecretKeySpec(secretKeyStream, ENCRYPTION_ALGO);
		} catch (final Exception e) {
			throw new GeneralSecurityException(ENCRYPTION_FAILURE, e);
		}
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
	private static byte[] generateSaltBytes() {
		final SecureRandom random = new SecureRandom();
		final byte[] bytes = new byte[SALT_LENGTH];
		random.nextBytes(bytes);
		return bytes;
	}
}
