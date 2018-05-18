package com.nonobank.test.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
public class URI {
    private static final String FILE_SEPATATOR = System.getProperty("file.separator");
    public final String env;
    public final String appName;
    public final String interfaceName;
    public final Map<String, String> map;

    public URI(String env, String appName, String interfaceName) {
        this.env = env;
        this.appName = appName;
        this.interfaceName = interfaceName;
        map = new HashMap<>();
        initMap();
    }

    private void initMap() {
        map.put("env", env);
        map.put("appName", appName);
        map.put("interfaceName", interfaceName);
    }

    public String toString() {
        String temp = interfaceName.replace(FILE_SEPATATOR, "_");
        if ("stb".equalsIgnoreCase(env) || "sit".equalsIgnoreCase(env)) {
            return FILE_SEPATATOR + env.toLowerCase() + FILE_SEPATATOR + appName + FILE_SEPATATOR + temp + ".json";
        }
        return FILE_SEPATATOR + appName + FILE_SEPATATOR + temp + ".json";
    }
}
