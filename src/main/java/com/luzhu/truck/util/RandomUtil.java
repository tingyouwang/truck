package com.luzhu.truck.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    public static Random rand;
    static {
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }

    }
    public static String numEnglishMixRandom(int size, boolean isUpperCase) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < size; ++i) {
            boolean isNum = rand.nextBoolean();
            if (isNum) {
                sb.append(rand.nextInt(10));
            } else {
                char ch = (char)(rand.nextInt(26) + 97);
                sb.append(ch);
            }
        }

        return transform(sb.toString(), isUpperCase);
    }
    private static String transform(String text, boolean isUpperCase) {
        return isUpperCase ? text.toUpperCase() : text;
    }
}
