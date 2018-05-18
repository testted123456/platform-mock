package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/24.
 */
public interface Code {
    public interface Res {
        public static final String SUCCESS = "0000000";    //成功
        public static final String UNKNOWN_ERROR = "9999999"; //未知错误
        public static final String VALID_ERROR    ="0000007";//参数校验错误
    }

    public interface Valid{
        public static final  String  IS_NULL = "param is null";
        public static final  String  NOT_MATCH ="string is not json or xml";
    }
}
