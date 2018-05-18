package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/25.
 */
public class ResponseResult<T> {


    private static final String SUCCESS_CODE = Code.Res.SUCCESS;

    private String errorCode;
    private String errorMessage;
    private T data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseResult<T> error(String errorCode, Object errorMessage) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage.toString());
        return result;

    }

    public static <T> ResponseResult<T> success(String message, T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setErrorCode(SUCCESS_CODE);
        result.setErrorMessage(message);
        result.setData(data);
        return result;
    }

    @Override
    public String toString() {
        if (data != null) {
            return "ResponseResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", data=" + data.toString() + "]";
        }
        return "ResponseResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
    }

    public static class Void {
        private Void() {
        }

        public static final Void V = null;
    }
}
