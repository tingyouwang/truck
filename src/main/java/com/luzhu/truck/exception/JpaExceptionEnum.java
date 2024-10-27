package com.luzhu.truck.exception;

public enum JpaExceptionEnum implements IRuntimeExceptionEnum{
    PARAM_ERROR("JPA_0001", "PARAM_ERROR", "參數錯誤"),
    DATA_ERROR("JPA_0002", "DATA_ERROR", "資料錯誤"),
    JDBC_CLASS_CAST_ERROR("JPA_1001", "JDBC_CLASS_CAST_ERROR", "資料庫型別轉換Dto失敗"),
    SYSTEM_ERROR("JPA_9999", "UNKNOWN_FATAL_ERROR", "系統錯誤");

    private String code;
    private String message;
    private String memo;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMemo() {
        return this.memo;
    }

    private JpaExceptionEnum(final String code, final String message, final String memo) {
        this.code = code;
        this.message = message;
        this.memo = memo;
    }
}
