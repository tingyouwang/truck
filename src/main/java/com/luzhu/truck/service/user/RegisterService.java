package com.luzhu.truck.service.user;

import com.luzhu.truck.constant.RoleConst;
import com.luzhu.truck.dao.user.JwtSecretTokenDao;
import com.luzhu.truck.dao.user.UserOperatorDao;
import com.luzhu.truck.dto.GeoInfo;
import com.luzhu.truck.dto.user.LoginResDto;
import com.luzhu.truck.dto.user.RegisterDto;
import com.luzhu.truck.entity.user.JwtSecretToken;
import com.luzhu.truck.entity.user.UserOperator;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.GeneratorUtil;
import com.luzhu.truck.util.SecretSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class RegisterService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private UserOperatorDao userOperatorDao;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtSecretTokenDao jwtSecretTokenDao;
    @Transactional
    public LoginResDto register(RegisterDto reqDto, GeoInfo geoInfo) {

//        checkRegisterParam(reqDto);
//        String modeAccount = UserUtil.getAccount(reqDto.getRegisterMode(), reqDto.getMail(),
//                reqDto.getPhoneCountry(), reqDto.getPhone());
//        UserMember inviter = checkInviteCodeExistAndReturnUpperAgent(reqDto.getInviteCode());
//        verifyService.checkVerifyCode(VerifyConst.ACTION_REGISTER, modeAccount,
//                reqDto.getVerifyCode());

        UserOperator operator = createByNormal(reqDto, geoInfo);
        JwtSecretToken jwtSecretToken = new JwtSecretToken();
        jwtSecretToken.setUserId(reqDto.getUserId());
        jwtSecretToken.setSecretKey(GeneratorUtil.tokenSecret());
        jwtSecretTokenDao.save(jwtSecretToken);
//        verifyService.deleteVerifyCode(VerifyConst.ACTION_REGISTER, modeAccount);

        return userService.buildLoginResDto(operator);
    }

    /**
     * 創建一般帳號
     *
     * @param reqDto
     * @return
     */
    public UserOperator createByNormal(RegisterDto reqDto, GeoInfo geoInfo) {
        return create(reqDto, geoInfo);
    }

    private UserOperator create(RegisterDto reqDto, GeoInfo geoInfo) {

        UserOperator operator = saveUserOperator(reqDto, geoInfo);
//        saveUserMember(operator.getId(), inviter, geoInfo);
//        saveUserMemberTree(operator.getId(), inviter);
//        saveUserMemberWallet(operator.getId());
//        saveRecordUserMember(operator.getId());
//        saveUserMemberSetting(operator.getId());

        return operator;
    }

    private UserOperator saveUserOperator(RegisterDto reqDto, GeoInfo geoInfo) {
        String salt = GeneratorUtil.salt();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        long nowEpoch = DateTimeUtil.toUtcEpochSecond(now);

        UserOperator operator = new UserOperator();
        operator.setUserId(reqDto.getUserId());
        operator.setRole(RoleConst.ADMIN);
        operator.setCreatedTime(nowEpoch);
        operator.setUpdatedTime(nowEpoch);
//        operator.setNickName(reqDto.getNickName());
//        operator.setThirdPartType(thirdPart);
//        operator.setRegisterMode(reqDto.getRegisterMode());
//        if (RegisterModeConst.MAIL.equals(reqDto.getRegisterMode())) {
//            operator.setMail(reqDto.getMail());
//            operator.setPhoneCountry(null);
//            operator.setPhone(null);
//        } else {
//            operator.setMail(null);
//            operator.setPhoneCountry(reqDto.getPhoneCountry());
//            operator.setPhone(reqDto.getPhone());
//        }
        operator.setSalt(salt);
        operator.setPassword(SecretSecurityUtil.passwordEncode(reqDto.getPassword(), salt));
        operator.setLastLoginTime(nowEpoch);
        operator.setLastLoginIp(geoInfo.getIp());
        return userOperatorDao.save(operator);
    }
}
