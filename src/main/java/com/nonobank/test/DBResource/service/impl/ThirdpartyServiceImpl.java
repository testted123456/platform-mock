/*
package com.nonobank.test.DBResource.service.impl;

import com.nonobank.test.DBResource.entity.Thirdparty;
import com.nonobank.test.DBResource.repository.ThirdpartyRepository;
import com.nonobank.test.DBResource.service.ThirdpatryService;
import com.nonobank.test.entity.MockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

*/
/**
 * Created by H.W. on 2018/5/24.
 *//*

@Service
public class ThirdpartyServiceImpl implements ThirdpatryService {
    @Autowired
    private ThirdpartyRepository repository;

    @Override
    public Thirdparty add(Thirdparty thirdparty) throws MockException {
        if (thirdparty.getId() == null) {
            if (repository.findThirdpartyByName(thirdparty.getName()) != null) {
                throw new MockException(thirdparty.getName() + "应用已存在");
            }
        }
        return repository.save(thirdparty);
    }

    @Override
    public List<Thirdparty> getAll(){
        return repository.findAll();
    }

    public Thirdparty findThirdpartyByName(String name){
        return repository.findThirdpartyByName(name);
    }

}
*/
