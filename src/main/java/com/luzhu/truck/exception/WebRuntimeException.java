package com.luzhu.truck.exception;

public class WebRuntimeException extends AbstractException{
    private static final long serialVersionUID = 6802948709543718808L;

    public WebRuntimeException(String code, String message) {
        super(code, message);
    }

    public WebRuntimeException(IRuntimeExceptionEnum iRuntimeExceptionEnum) {
        super(iRuntimeExceptionEnum);
    }

    public static WebRuntimeException systemError() {
        return new WebRuntimeException(WebExceptionEnum.SYSTEM_ERROR);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof WebRuntimeException)) {
            return false;
        } else {
            WebRuntimeException other = (WebRuntimeException)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WebRuntimeException;
    }

    public int hashCode() {
//        int result = true;
        return 1;
    }

    public String toString() {
        return "WebRuntimeException()";
    }
}
