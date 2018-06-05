package com.nonobank.test.DBResource.entity;

/**
 * Created by H.W. on 2018/4/25.
 */
public class Result {
    private String msg;
    private int code;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(Code.ResultCode.SUCCESS.getCode());
        result.setMsg(Code.ResultCode.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(Code.ResultCode.SUCCESS.getCode());
        result.setMsg(Code.ResultCode.SUCCESS.getMsg());
        return result;
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}


