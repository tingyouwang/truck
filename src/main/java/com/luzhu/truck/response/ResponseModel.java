package com.luzhu.truck.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class ResponseModel<T> {

    private static final long serialVersionUID = -7147650333527797098L;
    private String code;
    private String message;
    private T data;
    @JsonIgnore
    private Boolean printLog;

    public ResponseModel() {
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMessage(ResponseEnum.SUCCESS.getMessage());
        this.printLog = Boolean.FALSE;
    }

    public ResponseModel(ResponseEnum responseEnum) {
        this.setCode(responseEnum.getCode());
        this.setMessage(responseEnum.getMessage());
        this.printLog = Boolean.FALSE;
    }

    public ResponseModel(T data) {
        this.setData(data);
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMessage(ResponseEnum.SUCCESS.getMessage());
        this.printLog = Boolean.FALSE;
    }

    public ResponseModel(T data, boolean printLog) {
        this.setData(data);
        this.setCode(ResponseEnum.SUCCESS.getCode());
        this.setMessage(ResponseEnum.SUCCESS.getMessage());
        this.printLog = printLog;
    }

    public static <T> ResponseModelBuilder<T> builder() {
        return new ResponseModelBuilder();
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public Boolean getPrintLog() {
        return this.printLog;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setData(final T data) {
        this.data = data;
    }

    @JsonIgnore
    public void setPrintLog(final Boolean printLog) {
        this.printLog = printLog;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResponseModel)) {
            return false;
        } else {
            ResponseModel<?> other = (ResponseModel) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59:
                {
                    Object this$printLog = this.getPrintLog();
                    Object other$printLog = other.getPrintLog();
                    if (this$printLog == null) {
                        if (other$printLog == null) {
                            break label59;
                        }
                    } else if (this$printLog.equals(other$printLog)) {
                        break label59;
                    }

                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResponseModel;
    }

//    public int hashCode() {
//        int PRIME = true;
//        int result = 1;
//        Object $printLog = this.getPrintLog();
//        result = result * 59 + ($printLog == null ? 43 : $printLog.hashCode());
//        Object $code = this.getCode();
//        result = result * 59 + ($code == null ? 43 : $code.hashCode());
//        Object $message = this.getMessage();
//        result = result * 59 + ($message == null ? 43 : $message.hashCode());
//        Object $data = this.getData();
//        result = result * 59 + ($data == null ? 43 : $data.hashCode());
//        return result;
//    }

    public String toString() {
        String var10000 = this.getCode();
        return "ResponseModel(code=" + var10000 + ", message=" + this.getMessage() + ", data=" + this.getData() + ", printLog=" + this.getPrintLog() + ")";
    }

    public ResponseModel(final String code, final String message, final T data, final Boolean printLog) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.printLog = printLog;
    }

    public ResponseModel validFail(String errorMessage, ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = errorMessage;
        this.printLog = false;

        return this;
    }

    public static class ResponseModelBuilder<T> {
        private String code;
        private String message;
        private T data;
        private Boolean printLog;

        ResponseModelBuilder() {
        }

        public ResponseModelBuilder<T> code(final String code) {
            this.code = code;
            return this;
        }

        public ResponseModelBuilder<T> message(final String message) {
            this.message = message;
            return this;
        }

        public ResponseModelBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        @JsonIgnore
        public ResponseModelBuilder<T> printLog(final Boolean printLog) {
            this.printLog = printLog;
            return this;
        }

        public ResponseModel<T> build() {
            return new ResponseModel(this.code, this.message, this.data, this.printLog);
        }

        public String toString() {
            return "ResponseModel.ResponseModelBuilder(code=" + this.code + ", message=" + this.message + ", data=" + this.data + ", printLog=" + this.printLog + ")";
        }
    }

}
