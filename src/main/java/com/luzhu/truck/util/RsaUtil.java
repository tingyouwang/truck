package com.luzhu.truck.util;

import com.luzhu.truck.exception.ToolsException;
import com.luzhu.truck.exception.ToolsExceptionEnum;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtil {
    private RsaUtil() {
    }

    public static RsaKeyPair generateKeyPair() {
        try {
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, sr);
            KeyPair kp = kpg.generateKeyPair();
            Key privateKey = kp.getPrivate();
            byte[] privateKeyBytes = privateKey.getEncoded();
            String pri = Base64Util.encode(privateKeyBytes);
            Key publicKey = kp.getPublic();
            byte[] publicKeyBytes = publicKey.getEncoded();
            String pub = Base64Util.encode(publicKeyBytes);
            RsaKeyPair keyPair = new RsaKeyPair();
            keyPair.setPrivateKey(pri);
            keyPair.setPublicKey(pub);
            RSAPublicKey rsp = (RSAPublicKey)kp.getPublic();
            BigInteger bint = rsp.getModulus();
            byte[] b = bint.toByteArray();
            byte[] deBase64Value = Base64.encodeBase64(b);
            String retValue = new String(deBase64Value);
            keyPair.setModulus(retValue);
            return keyPair;
        } catch (NoSuchAlgorithmException var15) {
            var15.printStackTrace();
            throw new ToolsException(ToolsExceptionEnum.RSA_GENERATE_ERROR);
        }
    }

    public static String encrypt(String plainText, String publicKey) {
        try {
            byte[] decoded = Base64Util.decodeBytes(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, pubKey);
            byte[] dataByte = Base64Util.encodeBytes(plainText);
            int inputLen = dataByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * 117) {
                byte[] cache;
                if (inputLen - offSet > 117) {
                    cache = cipher.doFinal(dataByte, offSet, 117);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] encryptedData = out.toByteArray();
            out.close();
            return new String(Base64Util.encode(encryptedData));
        } catch (Exception var12) {
            var12.printStackTrace();
            throw new ToolsException(ToolsExceptionEnum.RSA_ENCRYPT_ERROR);
        }
    }

    public static String decrypt(String encryptText, String privateKey) {
        try {
            RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decodeBytes(privateKey)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, priKey);
            byte[] encryptByte = Base64Util.decodeBytes(encryptText);
            int inputLen = encryptByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for(int i = 0; inputLen - offSet > 0; offSet = i * 128) {
                byte[] cache;
                if (inputLen - offSet > 128) {
                    cache = cipher.doFinal(encryptByte, offSet, 128);
                } else {
                    cache = cipher.doFinal(encryptByte, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] decryptedByte = out.toByteArray();
            out.close();
            return Base64Util.decode(new String(decryptedByte, StandardCharsets.UTF_8));
        } catch (Exception var11) {
            var11.printStackTrace();
            throw new ToolsException(ToolsExceptionEnum.RSA_DECRYPT_ERROR);
        }
    }

    public static String sign(String plainText, String privateKey) throws Exception {
        try {
            byte[] keyBytes = Base64Util.decodeBytes(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateK);
            signature.update(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64Util.encode(signature.sign());
        } catch (Exception var7) {
            var7.printStackTrace();
            throw var7;
        }
    }

    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        try {
            byte[] keyBytes = Base64Util.decodeBytes(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicK);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64Util.decodeBytes(sign));
        } catch (Exception var8) {
            var8.printStackTrace();
            throw var8;
        }
    }

    public static void main(String[] args) throws Exception {

//        RsaKeyPair rsaKeyPair = generateKeyPair();
//        RsaKeyPair rsaKeyPair2 = generateKeyPair();
//        RsaKeyPair rsaKeyPair3 = generateKeyPair();

        String plainText = "!QAz123";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDshjz42bQcMV8BfhvNK3CrWKRarvHXyauk6sQtDG6BlNhB07qvQc0Q2qrT8jYwbeYix+QTmCZK/lLDfOAK+/TSd5YIiHgjY34sQzVBGyAI36vAJZIuxAjbWCi5cvhzT1SHqLyhR9URrWsBlMnaOZLlJ53t0Gt77/pe/n1oXsPjUwIDAQAB";
        String encrypt = encrypt(plainText, publicKey);
        System.out.println(encrypt);
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOyGPPjZtBwxXwF+G80rcKtYpFqu8dfJq6TqxC0MboGU2EHTuq9BzRDaqtPyNjBt5iLH5BOYJkr+UsN84Ar79NJ3lgiIeCNjfixDNUEbIAjfq8Alki7ECNtYKLly+HNPVIeovKFH1RGtawGUydo5kuUnne3Qa3vv+l7+fWhew+NTAgMBAAECgYAM2OlXX3aLe0PufmEYNd5JEFmJgMiAzTgrjxCDDz0kDvXoLXGNz3ZKDoTRWl/N0EpfnURjx6HrEVa6aPAG6LZq3JHBGjhb8e2diUIO8yPG5We1AAb9kP6W5wY2wkBrtCDy2VqUMFquRCXOjivElONpRD+ajNd7Nwn8K1Lbe7gsoQJBAO0leNWV0SjAgUg9lM35oRe5lZzZv8GbBEgl7QdWbw8eB8TE6tzHYvk6vnmjNKunH6tShNNB5RgLUXTGAOP1L5kCQQD/VBtTqO/9SyNyR2tdPRRzbUlpmydV70ceBqIycqbuGnp7r0WCUr1g+FeICdOk91SevCmn8wq7f0ZN59EOi23LAkA+y69lj1hlXCRfxr7ClBmOOLxfXmGimLPXjwNm8AJUlHUmK6atc7bHnECX4RmvYn/GVAGMF2TcqxKUw2G0+1XxAkBfOYT/jJAEzZMouc4m9fK8odOxWTYwbI7/ecXgPrZXMvTfUB0CX81VbwlmKRQQAFZE5TQ7+P7VssALqhtj60hFAkEAtdxMPR/rIdO32Wkx4LNJ6BqBAof4h4JU6REhA0Otaf7IV71vmIZ+whRuSpJpdrZ4eHlZl2Nezy0prwtl+s6Qzw==";
        String decrypt = decrypt(encrypt, privateKey);
        System.out.println(decrypt);

        String x = "x";
    }
}
