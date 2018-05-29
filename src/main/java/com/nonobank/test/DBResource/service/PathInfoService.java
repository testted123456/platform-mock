package com.nonobank.test.DBResource.service;


import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.entity.MockException;

import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
public interface PathInfoService {
    public List<PathInfo> getAll();
    public PathInfo add(PathInfo pathInfo) throws MockException;
    public List<PathInfo> add(List<PathInfo> pathInfos);
    public PathInfo getPathInfoById(Long id);
}
