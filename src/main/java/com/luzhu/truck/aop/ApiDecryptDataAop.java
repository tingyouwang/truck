package com.luzhu.truck.aop;

import com.luzhu.truck.annotation.DecryptParam;
import com.luzhu.truck.exception.*;
import com.luzhu.truck.util.RsaKeyPair;
import com.luzhu.truck.util.RsaUtil;
import com.luzhu.truck.validator.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

public class ApiDecryptDataAop extends BaseAop {

    private static final Logger log = LoggerFactory.getLogger(ApiDecryptDataAop.class);
    @Value("${wanda.switch.need-encrypt:true}")
    private boolean needEncrypt;
//    @Autowired
//    private RedisService redisService;
    private static final String API_SECRET_KEY = "api-secret-key";

    public ApiDecryptDataAop() {
    }

    @Pointcut("@annotation(com.wanda.web.annotation.ApiDecryptData)")
    public void apiDecryptDataAnnotation() {
    }

    @Before("apiDecryptDataAnnotation()")
    public void decryptData(JoinPoint joinPoint) {
        if (this.needEncrypt) {
            HttpServletRequest request = this.getHttpServletRequest();

            Validator.isNullThrow(request, WebRuntimeException.systemError());
            String apiSecretKey = request.getHeader("api-secret-key");

            Validator.isNullThrow(apiSecretKey, new WebRuntimeException(WebExceptionEnum.API_SECRET_KEY_NOT_EXIST));

//            RsaKeyPair rsaKeyPair = (RsaKeyPair) this.redisService.hashGetAll(WebRedisUtil.apiSecretKey(apiSecretKey), RsaKeyPair.class);
            RsaKeyPair rsaKeyPair = new RsaKeyPair();
            //todo 從redis or mysql 取得
            rsaKeyPair.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDshjz42bQcMV8BfhvNK3CrWKRarvHXyauk6sQtDG6BlNhB07qvQc0Q2qrT8jYwbeYix+QTmCZK/lLDfOAK+/TSd5YIiHgjY34sQzVBGyAI36vAJZIuxAjbWCi5cvhzT1SHqLyhR9URrWsBlMnaOZLlJ53t0Gt77/pe/n1oXsPjUwIDAQAB");
            rsaKeyPair.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOyGPPjZtBwxXwF+G80rcKtYpFqu8dfJq6TqxC0MboGU2EHTuq9BzRDaqtPyNjBt5iLH5BOYJkr+UsN84Ar79NJ3lgiIeCNjfixDNUEbIAjfq8Alki7ECNtYKLly+HNPVIeovKFH1RGtawGUydo5kuUnne3Qa3vv+l7+fWhew+NTAgMBAAECgYAM2OlXX3aLe0PufmEYNd5JEFmJgMiAzTgrjxCDDz0kDvXoLXGNz3ZKDoTRWl/N0EpfnURjx6HrEVa6aPAG6LZq3JHBGjhb8e2diUIO8yPG5We1AAb9kP6W5wY2wkBrtCDy2VqUMFquRCXOjivElONpRD+ajNd7Nwn8K1Lbe7gsoQJBAO0leNWV0SjAgUg9lM35oRe5lZzZv8GbBEgl7QdWbw8eB8TE6tzHYvk6vnmjNKunH6tShNNB5RgLUXTGAOP1L5kCQQD/VBtTqO/9SyNyR2tdPRRzbUlpmydV70ceBqIycqbuGnp7r0WCUr1g+FeICdOk91SevCmn8wq7f0ZN59EOi23LAkA+y69lj1hlXCRfxr7ClBmOOLxfXmGimLPXjwNm8AJUlHUmK6atc7bHnECX4RmvYn/GVAGMF2TcqxKUw2G0+1XxAkBfOYT/jJAEzZMouc4m9fK8odOxWTYwbI7/ecXgPrZXMvTfUB0CX81VbwlmKRQQAFZE5TQ7+P7VssALqhtj60hFAkEAtdxMPR/rIdO32Wkx4LNJ6BqBAof4h4JU6REhA0Otaf7IV71vmIZ+whRuSpJpdrZ4eHlZl2Nezy0prwtl+s6Qzw==");

            Validator.isNullThrow(rsaKeyPair, new WebRuntimeException(WebExceptionEnum.API_SECRET_KEY_EXPIRED));

            String uri = request.getRequestURI();
            Object[] objectArr = joinPoint.getArgs();
            Object[] var7 = objectArr;
            int var8 = objectArr.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Object paramObject = var7[var9];
                Field[] fieldArr = paramObject.getClass().getDeclaredFields();
                Field[] var12 = fieldArr;
                int var13 = fieldArr.length;

                for (int var14 = 0; var14 < var13; ++var14) {
                    Field field = var12[var14];
                    DecryptParam decryptParam = (DecryptParam) field.getAnnotation(DecryptParam.class);
                    if (null != decryptParam) {
                        this.decryptText(paramObject, field, rsaKeyPair);
                    }
                }

                log.debug("[參數解密]請求-uri：{}，參數：{}", uri, paramObject);
            }

        }
    }

    private void decryptText(Object paramObject, Field field, RsaKeyPair rsaKeyPair) {
        try {
            field.setAccessible(true);
            if (null != field.get(paramObject) && StringUtils.hasText(field.get(paramObject).toString())) {
                String plaintext = RsaUtil.decrypt(field.get(paramObject).toString(), rsaKeyPair.getPrivateKey());
                field.set(paramObject, plaintext);
            }
        } catch (AbstractException var5) {
            throw var5;
        } catch (Exception var6) {
            log.error("[參數解密]解密失敗:[{}]", var6.getMessage());
            throw WebRuntimeException.systemError();
        }
    }
}
