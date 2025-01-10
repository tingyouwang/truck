package com.luzhu.truck.exception;

public class ToolsException extends AbstractException {
    private static final long serialVersionUID = 6802948709543718808L;

    public ToolsException(String code, String message) {
        super(code, message);
    }

    public ToolsException(IRuntimeExceptionEnum iRuntimeExceptionEnum) {
        super(iRuntimeExceptionEnum);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ToolsException)) {
            return false;
        } else {
            ToolsException other = (ToolsException)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ToolsException;
    }

    public int hashCode() {
//        int result = true;
        return 1;
    }

    public String toString() {
        return "ToolsException()";
    }

}
