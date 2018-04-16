package com.nonobank.test.commons;

/**
 * Created by H.W. on 2018/4/10.
 */
public class MockException extends Exception{
    public MockException(){}

    /**
     * 异常描述
     * @param arg0
     */
    public MockException(final String arg0){super(arg0);}
}