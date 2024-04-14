package com.luzhu.truck.exception;

public class AppException extends AbstractException{
    public AppException(String code, String message) {
        super(code, message);
    }
    public AppException(IRuntimeExceptionEnum iRuntimeExceptionEnum) {
        super(iRuntimeExceptionEnum);
    }
}
