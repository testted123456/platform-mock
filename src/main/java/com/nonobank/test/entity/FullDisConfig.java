package com.nonobank.test.entity;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by H.W. on 2018/5/4.
 */


public class FullDisConfig {
    private Map<String, String> interMap;
    private Map<String, Map<String, String>> appIPMap;


    public Map<String, String> getInterMap() {
        return interMap;
    }

    public void setInterMap(Map<String, String> interMap) {
        if (this.interMap == null) {
            this.interMap = interMap;
        } else {
            this.interMap.putAll(interMap);
        }
    }

    public Map<String, Map<String, String>> getAppIPMap() {
        return appIPMap;
    }

    public void setAppIPMap(Map<String, Map<String, String>> appIPMap) {
        if (this.appIPMap == null) {
            this.appIPMap = appIPMap;
        } else {
            this.appIPMap.putAll(appIPMap);
        }
    }
}
