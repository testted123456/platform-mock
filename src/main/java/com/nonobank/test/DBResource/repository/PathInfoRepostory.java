package com.nonobank.test.DBResource.repository;

import com.nonobank.test.DBResource.entity.PathInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
public interface PathInfoRepostory extends JpaRepository<PathInfo,Long> {
    public PathInfo getPathInfoByName(String pathName);
    public PathInfo getPathInfoByFullName(String fullName);
    public List<PathInfo>  findByPid(Long pid);
}
