package com.nonobank.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Service;

/**
 * Created by H.W. on 2018/5/29.
 */

@Service
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccessDecisionManager accessDecisionManager;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager);

    }
}
