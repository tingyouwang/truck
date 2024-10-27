package com.luzhu.truck.exception;

public class JpaRuntimeException extends AbstractException{
    private static final long serialVersionUID = 6802948709543718808L;

    public JpaRuntimeException(String code, String message) {
        super(code, message);
    }

    public JpaRuntimeException(IRuntimeExceptionEnum iRuntimeExceptionEnum) {
        super(iRuntimeExceptionEnum);
    }

    public static JpaRuntimeException systemError() {
        return new JpaRuntimeException(JpaExceptionEnum.SYSTEM_ERROR);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JpaRuntimeException)) {
            return false;
        } else {
            JpaRuntimeException other = (JpaRuntimeException)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JpaRuntimeException;
    }

    public int hashCode() {
//        int result = true;
        return 1;
    }

    public String toString() {
        return "JpaRuntimeException()";
    }
}
