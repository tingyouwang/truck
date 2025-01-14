package com.luzhu.truck.service.user;

import com.alibaba.fastjson.JSONObject;
import com.luzhu.truck.constant.ExpireTimeConst;
import com.luzhu.truck.constant.RoleConst;
import com.luzhu.truck.dao.user.JwtSecretTokenDao;
import com.luzhu.truck.dao.user.UserOperatorDao;
import com.luzhu.truck.dto.GeoInfo;
import com.luzhu.truck.dto.user.LoginReqDto;
import com.luzhu.truck.dto.user.LoginResDto;
import com.luzhu.truck.entity.user.UserOperator;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.exception.UserExceptionEnum;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.JwtUtil;
import com.luzhu.truck.util.SecretSecurityUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class UserService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Value("${env.token.secret}")
    private String tokenSecret;
    @Autowired
    private JwtSecretTokenDao jwtSecretTokenDao;
    @Autowired
    private UserOperatorDao userOperatorDao;
    public LoginResDto buildLoginResDto(UserOperator userOperator) {

//        MemberUser memberUser = new MemberUser();
//        memberUser.setRole(RoleConst.ADMIN);
//        memberUser.setUserId(userOperator.getId());
//        memberUser.setRegisterMode(userOperator.getRegisterMode());
//        memberUser.setMail(userOperator.getMail());
//        memberUser.setPhoneCountry(userOperator.getPhoneCountry());
//        memberUser.setPhone(userOperator.getPhone());

        String token = getAdminToken(userOperator.getUserId(), new JSONObject());

        return LoginResDto.builder()
                .userId(userOperator.getUserId())
//                .nickName(userOperator.getNickName())
                .token(token)
                .build();
    }

    public String getAdminToken(String userId, JSONObject jsonObject) {
        return getToken(RoleConst.ADMIN, userId, false, jsonObject,
                ExpireTimeConst.ADMIN_TOKEN_TIME, null);
    }

//    public String getMemberToken(String userId, JSONObject jsonObject) {
//        return getToken(RoleConst.MEMBER, userId, true, jsonObject,
//                ExpireTimeConst.MEMBER_TOKEN_TIME, null);
//    }

    public String getToken(String role, String userId, boolean isNewToken, JSONObject jsonObject,
                           int expireTime, Map<String, String> tokenMap) {

//        String redisKey = RedisKeyUtil.userInfo(role, userId);
//        String tokenSecret = getTokenSecret(redisKey, isNewToken);
        String tokenSecret = getJwtSecretKey(userId);

//        jsonObject.put(TokenConst.TOKEN_SECRET, tokenSecret);
//        redisService.hashPutAll(redisKey, jsonObject);
//        if (expireTime > -1) {
//            redisService.expire(redisKey, expireTime, TimeUnit.MINUTES);
//        }

        if (null == tokenMap) {
            return JwtUtil.sign(tokenSecret, role, userId);
        } else {
            return JwtUtil.sign(tokenSecret, tokenMap);
        }
    }

    private String getJwtSecretKey(String userId) {
        //todo 未來改由redis取得
//        String tokenSecret = jwtSecretTokenDao.getSecretKey(userId);
//        if (!StringUtils.hasText(tokenSecret)) {
//            tokenSecret = GeneratorUtil.tokenSecret();
//        }
//        return tokenSecret;
        return tokenSecret;
    }

    @Transactional
    public LoginResDto login(LoginReqDto reqDto, GeoInfo geoInfo) {
        UserOperator operator = userOperatorDao.getUserById(reqDto.getUserId());
        if (null == operator) {
            throw new AppException(UserExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        if (!SecretSecurityUtil.passwordMatch(reqDto.getPassword(), operator.getSalt(),
                operator.getPassword())) {
            throw new AppException(UserExceptionEnum.PASSWORD_ERROR);
        }
        if ("DISABLE".equals(operator.getStatus())) {
            throw new AppException(UserExceptionEnum.ACCOUNT_STATUS_DISABLE);
        }

//        coreVerifyService.checkGoogleAuth(operator.getGoogleAuth(), operator.getSalt(),
//                reqDto.getGoogleCode(), false);

        int i = updateByLogin(operator.getUserId(), geoInfo.getIp());
        Validator.isFalseThrow(i == 1, new AppException(SystemExceptionEnum.UPDATE_ERROR));

        return buildLoginResDto(operator);
    }

    private int updateByLogin(String id, String ip) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);
        return userOperatorDao.updateByLogin(id, utcEpochSecond, ip);
    }
}
