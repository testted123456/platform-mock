package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/13.
 */
public class URI {
    private final String separator = "/";

    public final String env;
    public final String appName;
    public final String interfaceName;

    public URI(String env, String appName, String interName) {
        this.env = env;
        this.appName = appName;
        this.interfaceName = interName;
    }

    public String toString() {
        String temp = this.interfaceName.replace(separator, "_");

        if ("stb".equalsIgnoreCase(this.env) || "sit".equalsIgnoreCase(this.env)) {
            return this.env.toLowerCase() + separator + this.appName + separator + temp;
        }
        return this.appName + separator + temp;


    }
}
