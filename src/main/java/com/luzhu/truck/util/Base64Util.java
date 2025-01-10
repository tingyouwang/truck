package com.luzhu.truck.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Util {
    private Base64Util() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] encodeBytes(String str) {
        return Base64.encodeBase64(str.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decodeBytes(String str) {
        return Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    }

    public static String decode(byte[] bytes) {
        return new String(Base64.decodeBase64(bytes), StandardCharsets.UTF_8);
    }

    public static String encode(String str) {
        return new String(Base64.encodeBase64(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decode(String str) {
        return new String(Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static void main(String[] args) {
        String text = "dsadsa";
        String encodeText = encode(text);
        System.out.println(encodeText);
        System.out.println(decode(encodeText));
    }
}
