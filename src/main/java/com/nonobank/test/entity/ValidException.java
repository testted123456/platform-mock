package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/5/29.
 */
public class ValidException extends RuntimeException {
    private Code.ResultCode resultCode;

    public ValidException(String message){
        super(message);
        this.resultCode = Code.ResultCode.VALIDATION_ERROR;
    }
}
