package com.nonobank.test.entity;

/**
 * Created by H.W. on 2018/4/24.
 */
public enum Type {
    XML("application/xml", "xml", 1), JSON("application/json", "json", 2);


    private String contentType;
    private String dataType;
    private int id;

    private Type(String contentType, String dataType, int id) {
        this.contentType = contentType;
        this.dataType = dataType;
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDataType() {
        return dataType;
    }

    public int getId() {
        return id;
    }
}
