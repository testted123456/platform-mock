package com.nonobank.test.DBResource.service.impl;


import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.DBResource.repository.PathInfoRepostory;
import com.nonobank.test.DBResource.service.PathInfoService;
import com.nonobank.test.DBResource.entity.ValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
@Service
public class PathInfoServiceImpl implements PathInfoService {

    @Autowired
    private PathInfoRepostory repository;


    public List<PathInfo> findByPid(Long id) {
        return repository.findByPid(id);
    }

    @Override
    public PathInfo getPathInfoById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public PathInfo add(PathInfo pathInfo) {
        if (pathInfo.getId() == null) {
            String name = pathInfo.getName();
            if (repository.getPathInfoByName(name) != null) {
                throw new ValidException(name + "目录已存在");
            }
        }
        return repository.save(pathInfo);
    }

    @Override
    public List<PathInfo> add(List<PathInfo> pathInfos) {
        return repository.save(pathInfos);
    }

    @Override
    public List<PathInfo> getAll() {
        return repository.findAll();
    }
}
