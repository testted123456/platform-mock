package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/25.
 */
public class MockException extends Exception {
    public MockException() {
    }

    public MockException( String message) {
        super(message);
    }
    public MockException(int errcode, String message) {
        super(message);
    }

}
