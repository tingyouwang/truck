package com.luzhu.truck.dto.user;

import com.luzhu.truck.annotation.DecryptParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank
    private String userId;

//    @NotNull
//    @Pattern(regexp = "mail|phone", flags = Pattern.Flag.UNICODE_CASE)
//    private String registerMode;
//    // 信箱註冊 使用
//    @NotNull
//    private String mail;
//    // 電話註冊 使用
//    @NotNull
//    private String phoneCountry;
//    // 電話註冊 使用
//    @NotNull
//    private String phone;
//    // 驗證碼
//    @NotBlank
//    private String verifyCode;
    @NotBlank
    @DecryptParam
    private String password;

//    // 會員名稱
//    @NotNull
//    private String nickName;
//    // 邀請碼
//    @NotNull
//    private String inviteCode;
}
