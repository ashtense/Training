package com.ashwani.encryption;

public final class AESConstants {

	public static final String KEY_GENERATION_PASSWORD = "adpDbCryptKey#123";
	public static final String KEY_GENERATION_ALGORITHM = "PBKDF2WithHmacSHA512";
	public static final int ENCRYPTION_KEY_BIT_SIZE = 256;
	public static final String ENCRYPTION_ALGO_CONFIG = "AES/CBC/PKCS5Padding";
	public static final String ENCODING_STANDARD = "UTF-8";
	public static final String ENCRYPTION_ALGO = "AES";
	public static final int KEY_ITERATION_COUNT = 65556;
	public static final int SALT_LENGTH = 20;

	private AESConstants() {
	}
}
