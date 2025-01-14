package com.luzhu.truck.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class JwtUtil {
    public static DecodedJWT verify(String token) {
//        DecodedJWT decode = JWT.decode(token);

        //暫時hard code secret key
        Algorithm algorithm = Algorithm.HMAC256("kib0939rhfg52a1vc6044g20zq1wh03m");
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
    public static String sign(String secret, String role, String userId) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("role", role).withClaim("userId", userId)
                .withExpiresAt(Instant.ofEpochSecond(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(2).toEpochSecond(ZoneOffset.UTC)))
                .sign(algorithm);
    }
    public static String sign(String secret, Map<String, String> map) {
        return sign(secret, map, -1);
    }
    public static String sign(String secret, Map<String, String> map, int expireTime) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder jwtBuild = JWT.create();
        Iterator var5 = map.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var5.next();
            jwtBuild.withClaim((String)entry.getKey(), (String)entry.getValue());
        }

        if (expireTime > -1) {
            jwtBuild.withExpiresAt(new Date(System.currentTimeMillis() + (long)expireTime));
        }

        return jwtBuild.sign(algorithm);
    }
}
