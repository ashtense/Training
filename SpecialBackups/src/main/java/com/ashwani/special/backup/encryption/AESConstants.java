package com.ashwani.special.backup.encryption;

interface AESConstants {
    String KEY_GENERATION_PASSWORD = "adpDbCryptKey#123";
    String KEY_GENERATION_ALGORITHM = "PBKDF2WithHmacSHA512";
    int ENCRYPTION_KEY_BIT_SIZE = 256;
    String ENCRYPTION_ALGO_CONFIG = "AES/CBC/PKCS5Padding";
    String ENCODING_STANDARD = "UTF-8";
    String ENCRYPTION_ALGO = "AES";
    int KEY_ITERATION_COUNT = 65556;
    int SALT_LENGTH = 20;
}
