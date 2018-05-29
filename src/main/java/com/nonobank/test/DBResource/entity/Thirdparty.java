/*
package com.nonobank.test.DBResource.entity;


import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

*/
/**
 * Created by H.W. on 2018/5/23.
 *//*

@Entity
@Table(name = "mock_thirdparty")
public class Thirdparty {
    @Id
    @GeneratedValue
    private Long id;


    @NotNull(message = "服务名不能为空")
    @Column(nullable = false, columnDefinition = "varchar(30) COMMENT 'mock服务的名称'", unique = true)
    private String name;

    @Column(columnDefinition = "varchar(256) COMMENT '服务描述信息'")
    private String description;

    @Column(columnDefinition = "bit(1) default 0  COMMENT '是否需要转发,0:不需要，1:需要'")
    private boolean needProxy;

    @Column(columnDefinition = "varchar(256) COMMENT '若needProxy为true,则该项需填写转发规则对应的IP,多个键值对以;分割'")
    private String proxyIP;
    @Column(columnDefinition = "varchar(256) COMMENT '异常响应mock配置'")
    private String config;

    @Column(insertable = false, updatable = false, columnDefinition = "timestamp comment '创建时间' ")
    @Generated(GenerationTime.INSERT)
    LocalDateTime createTime;

    @Column(insertable = false, updatable = false
            , columnDefinition = "timestamp  default current_timestamp comment '更新时间' ")
    LocalDateTime updateTime;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNeedProxy() {
        return needProxy;
    }

    public void setNeedProxy(boolean needProxy) {
        this.needProxy = needProxy;
    }

    public String getProxyIP() {
        return proxyIP;
    }

    public void setProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
*/
