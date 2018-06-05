package com.nonobank.test.DBResource.service;

import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.MockException;

import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
public interface InterfaceInfoService {
    public MockInterfaceInfo add(MockInterfaceInfo interfaceInfo) throws MockException;

    public List<MockInterfaceInfo> getAll();
}
