package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/24.
 */
public enum  Position {
    FULL(1,"全部处理"),HEAD(2,"头部"),TAIL(3,"尾部"),INCLUSIVE(4,"首尾");
    private int id;
    private String desc;
    private Position(int id,String desc){
        this.desc = desc;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
