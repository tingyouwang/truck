package com.luzhu.truck.util;

public class SecretSecurityUtil {
    private SecretSecurityUtil() {
    }

    public static String passwordEncode(String password, String salt) {
        return BCryptUtil.encode(format(password, salt));
    }

    public static boolean passwordMatch(String password, String salt, String encodedPassword) {
        return BCryptUtil.match(format(password, salt), encodedPassword);
    }

    private static String format(String plaintext, String salt) {
        return plaintext.concat("(").concat(salt).concat(")");
    }
}
