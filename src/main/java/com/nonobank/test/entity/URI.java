package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/13.
 */
public class URI<T,V,K> {
    public final T env;
    public final V appName;
    public final K interfaceName;
     public URI(T env, V appName, K interName){
         this.env = env;
         this.appName = appName;
         this.interfaceName =interName;
     }
}
