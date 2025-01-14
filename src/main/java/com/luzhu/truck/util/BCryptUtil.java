package com.luzhu.truck.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    private static final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();


    private BCryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String encode(String password) {
        return bcryptPasswordEncoder.encode(password);
    }

    public static boolean match(String password, String encodedPassword) {
        return bcryptPasswordEncoder.matches(password, encodedPassword);
    }
}
