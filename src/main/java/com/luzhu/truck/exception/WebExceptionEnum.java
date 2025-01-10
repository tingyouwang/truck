package com.luzhu.truck.exception;

public enum WebExceptionEnum implements IRuntimeExceptionEnum{
    PARAM_ERROR("WEB_0001", "PARAM_ERROR", "參數錯誤"),
    DATA_ERROR("WEB_0002", "DATA_ERROR", "資料錯誤"),
    API_SECRET_KEY_NOT_EXIST("WEB_1001", "API_SECRET_KEY_NOT_EXIST", "API密鑰不存在"),
    API_SECRET_KEY_EXPIRED("WEB_1002", "API_SECRET_KEY_EXPIRED", "API密鑰已失效"),
    SYSTEM_ERROR("WEB_9999", "UNKNOWN_FATAL_ERROR", "系統錯誤");

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

    private WebExceptionEnum(final String code, final String message, final String memo) {
        this.code = code;
        this.message = message;
        this.memo = memo;
    }
}
