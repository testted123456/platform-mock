package com.nonobank.test.entity;

import com.nonobank.test.utils.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.executable.ValidateOnExecution;

/**
 * Created by H.W. on 2018/4/11.
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {
    private String httpCode;
    private String delayTime;
    private String errRate;

    public String getErrRate() {
        return errRate;
    }

    public void setErrRate(String errRate) {
        this.errRate = errRate;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public boolean isEmpty(){
        if (this ==null){
            return true;
        }
        if (StringUtils.isEmpty(httpCode)&&StringUtils.isEmpty(delayTime)
                &&StringUtils.isEmpty(errRate)){
            return true;

        }
    return false;
    }
}
