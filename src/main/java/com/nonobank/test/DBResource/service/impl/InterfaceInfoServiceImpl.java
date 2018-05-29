package com.nonobank.test.DBResource.service.impl;


import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.repository.InterfaceInfoRepository;
import com.nonobank.test.DBResource.service.InterfaceInfoService;
import com.nonobank.test.entity.MockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by H.W. on 2018/5/24.
 */
@Service
public class InterfaceInfoServiceImpl implements InterfaceInfoService {
    @Autowired
    private InterfaceInfoRepository repository;

    @Override
    public MockInterfaceInfo add(MockInterfaceInfo interfaceInfo) throws MockException {
        if (interfaceInfo.getId() == null) {
            String name = interfaceInfo.getUrl();
            if (repository.getMockInterfaceInfoByUrl(name) != null) {
                throw new MockException("接口已存在");
            }
        }
        return repository.save(interfaceInfo);
    }


    @Override
    public List<MockInterfaceInfo> getAll() {
        return repository.findAll();
    }

    public MockInterfaceInfo getMockInterfaceInfoById(Long id) {
        return repository.getOne(id);
    }

    public List<MockInterfaceInfo> findByPathInfoId(Long pid) {
        return repository.findByPathInfoId(pid);

    }

    public MockInterfaceInfo getMockInterfaceInfoByUrl(String name) {
        return repository.getMockInterfaceInfoByUrl(name);
    }
}
