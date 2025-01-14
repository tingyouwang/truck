package com.luzhu.truck.util;

public class GeneratorUtil {
    public static String salt() {
        return RandomUtil.numEnglishMixRandom(20, false);
    }

    public static String tokenSecret() {
        return RandomUtil.numEnglishMixRandom(32, false);
    }
}
