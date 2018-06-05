package com.nonobank.test.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by H.W. on 2018/5/29.
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {


    public static final String NO_LOGIN = "no login";
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        String url = ((FilterInvocation) object).getRequestUrl();
        if (((FilterInvocation) object).getRequest().getMethod().equals("OPTIONS")) {
            return;
        }
        if (url != null && !url.startsWith("/mock")) {
            return;
        }
        if (authentication.getPrincipal().toString().equals(ANONYMOUS_USER)) {
            throw new AccessDeniedException(NO_LOGIN);
        }
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        SecurityContextHolder.getContext().getAuthentication();
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
