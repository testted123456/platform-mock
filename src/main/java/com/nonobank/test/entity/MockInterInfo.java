package com.nonobank.test.entity;

import com.alibaba.fastjson.JSONObject;

import javax.validation.Valid;

/**
 * Created by H.W. on 2018/4/11.
 */
public class MockInterInfo {
    private String url;
    private String appName;
    private String interfaceName;
    private String desc;
    private JSONObject request;
    private JSONObject response;
    private String resType;
    private String rules;
    private String methodType;
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }



    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getResponse() {
        return response;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }


}
