package com.luzhu.truck.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserExceptionEnum implements IRuntimeExceptionEnum{
    ACCOUNT_NOT_EXIST("USER_01_01", "ACCOUNT_NOT_EXIST", "帳號不存在"),
    ACCOUNT_STATUS_DISABLE("USER_01_02", "ACCOUNT_STATUS_DISABLE", "帳號被關閉"),
    PASSWORD_ERROR("USER_01_03", "PASSWORD_ERROR", "密碼錯誤");
    private String code;
    private String message;
    private String memo;
}
