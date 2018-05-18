package com.nonobank.test.entity;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by H.W. on 2018/4/25.
 */
public class InterfaceInfo {
    private String url;
    private String env;
    private String appName;
    private String interfaceName;
    private String desc;
    private JSONObject request;
    private JSONObject response;
    private String resType;
    private String mothodType;
    private Config config;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getResponse() {
        return response;
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

    public String getMothodType() {
        return mothodType;
    }

    public void setMothodType(String mothodType) {
        this.mothodType = mothodType;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
