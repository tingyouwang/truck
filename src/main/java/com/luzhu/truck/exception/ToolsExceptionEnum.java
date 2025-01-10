package com.luzhu.truck.exception;

public enum ToolsExceptionEnum implements IRuntimeExceptionEnum {
    RSA_GENERATE_ERROR("T_1001", "RSA_GENERATE_ERROR"),
    RSA_ENCRYPT_ERROR("T_1002", "RSA_ENCRYPT_ERROR"),
    RSA_DECRYPT_ERROR("T_1003", "RSA_DECRYPT_ERROR"),
    SYSTEM_ERROR("T_9999", "UNKNOWN_FATAL_ERROR");

    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ToolsExceptionEnum(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
