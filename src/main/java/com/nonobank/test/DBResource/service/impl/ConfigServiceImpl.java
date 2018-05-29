/*
package com.nonobank.test.DBResource.service.impl;


import com.nonobank.test.DBResource.entity.Config;
import com.nonobank.test.DBResource.repository.ConfigRepository;
import com.nonobank.test.DBResource.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

*/
/**
 * Created by H.W. on 2018/5/24.
 *//*

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public List<Config> getAll() {
        return configRepository.findAll();
    }

    @Override
    public List<Config> add(Config config) {
        configRepository.save(config);
        return null;
    }
    public Config  getConfigByName(String name){
        return configRepository.getConfigByName(name);
    }
}
*/
