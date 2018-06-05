package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockException;

import com.nonobank.test.utils.IPUtil;
import com.nonobank.test.utils.JavaProxy;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by H.W. on 2018/4/27.
 */

@Component
public class ForwardRequest {


    @Autowired
    private ParseHttpServletRequest parseRequest;

    private static Map<String, Object> ipMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(ForwardRequest.class);

    public static void setIpMap(Map<String, Object> ipMap) {
        ForwardRequest.ipMap.putAll(ipMap);
    }


    public String forwardRequest(HttpServletRequest request, HttpServletResponse response) throws MockException, IOException {

        String urlStr = request.getRequestURI();
        String url = urlStr.replaceFirst("/web-mock", "");
        String host = IPUtil.getIpAddr(request);
        String contentType = request.getContentType();
        String method = request.getMethod();
        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/json";
        }

        String str = JSONObject.toJSONString(parseRequest.getRequestBody(request));
        String targetHost = (String) ipMap.get(host);
        if (StringUtils.isEmpty(targetHost)) {
            logger.info("配置中未找到转发主机信息，故走mock逻辑");
            return "-1";
        }
        url = "http://" + targetHost + url;
        logger.info("该请求无需mock,当前主机地址：" + host + "，  转发请求的地址" + url);
        response.setContentType(contentType);
        return JavaProxy.proxyCall(url, method, str, contentType);


    }

}
