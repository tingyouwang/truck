package com.luzhu.truck.response;

public enum ResponseEnum {
    SUCCESS("G_0000", "SUCCESS"),
    VALID_ERROR("G_0001", "VALID_ERROR"),
    DATABASE_DATA_ERROR("G_0002", "DATABASE_DATA_ERROR"),
    MAX_UPLOAD_SIZE_EXCEEDED("G_0003", "MAX_UPLOAD_SIZE_EXCEEDED"),
    REQUEST_FAIL("G_0004", "REQUEST_FAIL"),
    DATA_IS_EMPTY("G_0005", "查無符合資料"),
    SYSTEM_ERROR("G_9999", "UNKNOWN_FATAL_ERROR");

    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ResponseEnum(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
