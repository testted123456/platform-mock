package com.nonobank.test.commons;

/**
 * Created by H.W. on 2018/4/10.
 */
public enum Code {

    NOTNULL(1, "非空值为空"), NOT_JSON(2, "非JSON"),
    NOT_XML(3, "非XML"), NOT_JSON_OR_XML(4, "非JSON亦非XML"),
    ARGS_ILLEGAL(5, "入参值非法");

    private int code;
    private String des;

    private Code(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public int getCode() {
        return this.code;
    }
}
