package com.nonobank.test.entity;

import com.alibaba.fastjson.JSONObject;

import javax.validation.Valid;

/**
 * Created by H.W. on 2018/4/11.
 */
public class MockInterInfo {
    private String env;      //接口路径
    private String appName;   //mock名称
    private String interfaceName;   //接口名称
    private String desc;    //接口描述
    private JSONObject request;   //请求内容
    private JSONObject response;   //响应内容
    private String resType;      //响应格式，可设置为xml，默认json
    private String rules;
    private String methodType;   //接口请求类型
    private Config config;      //接口配置信息

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

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
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
