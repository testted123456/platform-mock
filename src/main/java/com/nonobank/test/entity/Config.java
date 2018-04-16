package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/11.
 *
 */
public class Config {
    private  String  httpCode;
    private  int   delayTime;
    private  double errRate;

    public double getErrRate() {
        return errRate;
    }
    public void setErrRate(double errRate) {
        this.errRate = errRate;
    }
    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }
}
