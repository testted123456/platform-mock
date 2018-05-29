package com.nonobank.test.DBResource.entity;

import java.util.List;

/**
 * Created by H.W. on 2018/5/28.
 */

public class NodeInfo {
    private Long id;
    private String name;
    private String type;
    private List<NodeInfo> list;

    public Long getId() {
        return id;
    }

    public NodeInfo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public NodeInfo setType(String type) {
        this.type = type;
        return this;
    }

    public List<NodeInfo> getList() {
        return list;
    }

    public void setList(List<NodeInfo> list) {
        this.list = list;
    }
}
