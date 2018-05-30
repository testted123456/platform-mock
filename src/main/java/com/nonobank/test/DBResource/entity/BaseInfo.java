package com.nonobank.test.DBResource.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by H.W. on 2018/5/30.
 */
public class BaseInfo {
    private Long id;
    private String name;
    private Long pid;
    private String pName;
    private Boolean needProxy;
    private String ipMap;
    private String config;
    private boolean directory;
    private List<BaseInfo> children = new ArrayList<>();

    public static BaseInfo createRoot() {
        BaseInfo root = new BaseInfo();
        root.setId(0l);
        return root;
    }

    public List<BaseInfo> getChildren() {
        return children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Boolean getNeedProxy() {
        return needProxy;
    }

    public void setNeedProxy(Boolean needProxy) {
        this.needProxy = needProxy;
    }

    public String getIpMap() {
        return ipMap;
    }

    public void setIpMap(String ipMap) {
        this.ipMap = ipMap;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public BaseInfo setDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public boolean isDirectory() {
        return directory;
    }
}
