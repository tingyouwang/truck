package com.luzhu.truck.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class UserOperator {
    @Id
    private String userId;
    // 角色
    private String role;
    // 創建時間
    private long createdTime;
    // 修改時間
    private long updatedTime;
//    // 暱稱
//    private String nickName = DefaultValueConst.EMPTY;
//    // 三方登入類型
//    private String thirdPartType = DefaultValueConst.NORMAL;
    // 註冊方式 手機:phone 信箱:mail 後台:backstage
    private String registerMode;
    // 信箱
    private String mail;
    // 電話國家
    private String phoneCountry;
    // 電話
    private String phone;
    // 帳號
    private String account;
    // 狀態 正常:enable 關閉:disable
    private String status = "enable";
    // 鹽
    private String salt;
    // 密碼
    private String password;
//    // 支付密碼
//    private String payPassword = DefaultValueConst.EMPTY;
//    // google驗證
//    private String googleAuth = DefaultValueConst.EMPTY;

    // 最後登入時間
    private long lastLoginTime;
    // 最後登入IP
    private String lastLoginIp;
////     備注
//    private String memo = DefaultValueConst.EMPTY;
}
