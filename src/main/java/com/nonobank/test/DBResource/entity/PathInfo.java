package com.nonobank.test.DBResource.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
@Entity
@Table(name = "mock_path_info")
public class PathInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '当前目录名' ")
    private String pathName;
    @Column(nullable = false, columnDefinition = "int COMMENT '父目录ID'")
    private Long pid;
    @Column(nullable = false, columnDefinition = "varchar(128) COMMENT '父目录名' ")
    private String previousName;

    @Column(nullable = false, columnDefinition = "varchar(512) COMMENT '当前位置的全路径' ")
    private String fullName;

    @Column(columnDefinition = "varchar(128) COMMENT '当前目录的配置项' ")
    private String config;

    @Column(columnDefinition = "bit(1) default 0  COMMENT '是否需要转发,0:不需要，1:需要'")
    private Boolean isProxy;

    @Column(columnDefinition = "varchar(512) COMMENT '转发ip键值对' ")
    private String ipMap;

    @OneToMany(mappedBy = "pathInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "status != 1")
    @JsonIgnore
    private List<MockInterfaceInfo> infos;

    @Column(columnDefinition = "smallint(1) default 0 COMMENT '0:正常，1:已删除'")
    private Short status;
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

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPreviousName() {
        return previousName;
    }

    public void setPreviousName(String previousName) {
        this.previousName = previousName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Boolean getProxy() {
        return isProxy;
    }

    public void setProxy(Boolean proxy) {
        isProxy = proxy;
    }

    public String getIpMap() {
        return ipMap;
    }

    public void setIpMap(String ipMap) {
        this.ipMap = ipMap;
    }

    public List<MockInterfaceInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<MockInterfaceInfo> infos) {
        this.infos = infos;
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
