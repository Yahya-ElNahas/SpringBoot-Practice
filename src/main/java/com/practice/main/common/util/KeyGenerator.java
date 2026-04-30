package com.practice.main.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public abstract class KeyGenerator {

    public static String generateEncodedKey() {
        byte[] bytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
