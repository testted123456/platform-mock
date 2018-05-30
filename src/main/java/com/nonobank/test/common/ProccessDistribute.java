package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.DataStruct;

import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Map;

/**
 * Created by H.W. on 2018/4/27.
 */

@Component
public class ProccessDistribute {


    private static Map<String, Object> ipMap;

    private static Logger logger = LoggerFactory.getLogger(ProccessDistribute.class);

    public static void setIpMap(Map<String, Object> ipMap) {
        ProccessDistribute.ipMap = ipMap;
    }
    public static void getOneDepthMap(String json,Map<String,Object> target) throws MockException {
        if (StringUtils.isEmpty(json)) {
            throw new MockException("请求内容不能为空");
        }
        Map<String, Object> map = null;
        if (json.contains("=")&&!(json.startsWith("{")||json.startsWith("<"))) {
            map = ProcessText.fromGetType(json);
        }else {
            json = ProcessText.getSpecTypeStr(json, Type.JSON);
            map = JSONObject.parseObject(json);
        }
        DataStruct.toOneDepth(map,target,"");
    }
    public String disposeRes(HttpServletRequest request, HttpServletResponse response) throws MockException, IOException {

        String urlStr = request.getRequestURI();
        String url = urlStr.replaceFirst("/web-mock", "");
        String host = ParseRequest.getIpAddr(request);
        String contentType = request.getContentType();
        String method = request.getMethod();
        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/json";
        }

        String str = AcquireInfo.getRequestBody(request);
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
