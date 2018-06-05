package com.nonobank.test.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by H.W. on 2018/5/31.
 */
@Component
public class OwnerApplicationRunner implements ApplicationRunner {
    @Autowired
    private PreserveConfig preserveConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        preserveConfig.initConfig();
    }
}
