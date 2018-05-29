package com.nonobank.test.DBResource.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by H.W. on 2018/5/23.
 */

@Entity
@Table(name = "mock_interface_info")
public class MockInterfaceInfo {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull(message = "接口名称不能为空")
    @Column(name = "interfaceName", nullable = false, columnDefinition = "varchar(255) COMMENT '被mock接口名称'")
    private String name;
    @NotNull(message = "接口全路径不能为空")
    @Column(name = "fullName", nullable = false, columnDefinition = "varchar(255) COMMENT '被mock接口的全路径'")
    private String url;
    @Column(columnDefinition = " smallint(1) default 0 COMMENT '0:application/json,1:application/xml' ")
    private Integer resType;
    @NotNull(message = "接口请求消息不能为空")
    @Column(nullable = false, columnDefinition = "varchar(2000) COMMENT '接口请求消息内容'")
    private String request;
    @NotNull(message = "接口响应内容不能为空")
    @Column(nullable = false, columnDefinition = "varchar(2000) COMMENT 'mock接口响应消息内容'")
    private String response;
    @Column(name = "description", columnDefinition = "varchar(256) COMMENT '接口简单描述'")
    private String desc;
    @Column(columnDefinition = "smallint(1) default 0 COMMENT '0:stb,1:sit' ")
    private Integer env;
    @Column(columnDefinition = "smallint(1) default 0 COMMENT '0:POST,1:GET' ")
    private Integer methodType;
    @Column(columnDefinition = "varchar(1024)  COMMENT '响应-请求数据对应规则' ")
    private String matchRule;
    @Column(columnDefinition = "varchar(256) COMMENT '异常响应配置信息'")
    private String config;
    @Column(columnDefinition = "int COMMENT '修改次数' ")
    private Integer version;
    @ManyToOne
    @JoinColumn(name = "path_Info_id")
    @JsonIgnore
    @Where(clause = "status != 1")
    private PathInfo pathInfo;
    @Column(columnDefinition = "smallint(1) default 0 COMMENT '0:正常，1:已删除'")
    private Short status;
    @Column(insertable = false, updatable = false, columnDefinition = "datetime comment '创建时间' ")
    @Generated(GenerationTime.INSERT)
    LocalDateTime createTime;

    @Column(insertable = false, updatable = false
            , columnDefinition = "datetime   comment '更新时间' ")
    @Generated(GenerationTime.ALWAYS)
    LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Integer getMethodType() {
        return methodType;
    }

    public void setMethodType(Integer methodType) {
        this.methodType = methodType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public PathInfo getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(PathInfo pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}
