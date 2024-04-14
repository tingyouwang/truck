package com.luzhu.truck.exception;

public class AbstractException extends RuntimeException {
    private String code;
    private Object data;
    public AbstractException() {

    }

    public AbstractException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AbstractException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }
    public AbstractException(IRuntimeExceptionEnum iRuntimeExceptionEnum) {
        this(iRuntimeExceptionEnum.getCode(), iRuntimeExceptionEnum.getMessage(), (Object)null);
    }

    public String getCode() {
        return this.code;
    }

    public Object getData() {
        return this.data;
    }
}
