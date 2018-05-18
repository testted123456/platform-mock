package com.nonobank.test.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/27.
 */
public class DisConfig {
  private String appName;
  private Map<String,String> ipMap;
  private List<String> interfaceName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Map<String, String> getIpMap() {
        return ipMap;
    }

    public void setIpMap(Map<String, String> ipMap) {
        this.ipMap = ipMap;
    }

    public List<String> getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(List<String> interfaceName) {
        this.interfaceName = interfaceName;
    }
}
