package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.FileUtil;
import com.nonobank.test.utils.FormatUtil;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/27.
 */

@Component
public class ProccessDistribute {

    @Autowired
    Assemble assemble;

    FullDisConfig fullDisConfig;

    private String fullName = "/JAVA_Files/web-mock/resource/config/FullDisConfig.json";

    private static Map<String, String> interMap;
    private static Map<String, Map<String, String>> appIPMap;

    private static Logger logger = LoggerFactory.getLogger(ProccessDistribute.class);

    public DisConfig setConfig(String str) {
        DisConfig disConfig = JSONObject.parseObject(str, DisConfig.class);

        init();

        List<String> list = disConfig.getInterfaceName();
        if (list.isEmpty()) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            String interfaceName = list.get(i).startsWith("/") ? list.get(i).substring(1) : list.get(i);
            interMap.put(interfaceName, disConfig.getAppName());
        }
        appIPMap.put(disConfig.getAppName(), disConfig.getIpMap());

        if (fullDisConfig == null) {
            fullDisConfig = new FullDisConfig();
        }
        fullDisConfig.setInterMap(interMap);
        fullDisConfig.setAppIPMap(appIPMap);
        try {
            FileUtil.createFile(fullName, FormatUtil.formatJson(JSONObject.toJSONString(fullDisConfig)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return disConfig;
    }


    public boolean delConfig(String str) {
        DisConfig disConfig = JSONObject.parseObject(str, DisConfig.class);
        List<String> list = disConfig.getInterfaceName();
        if (list.isEmpty()) {
            return true;
        }
        if (interMap != null) {
            for (int i = 0; i < list.size(); i++) {
                String interfaceName = list.get(i).startsWith("/") ? list.get(i).substring(1) : list.get(i);
                interMap.remove(interfaceName);
            }
        }
        if (appIPMap != null) {
            appIPMap.remove(disConfig.getAppName());
        }
        return true;
    }


    public String disposeRes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, MockException {
        logger.info("进入disposeRes方法");
        if (interMap == null && appIPMap == null) {
            init();
        }
        String url = request.getRequestURI();
        logger.info("url为" + url);
        url = url.replace("/web-mock/distribute", "");
        URI uri = parseUri(url);
        String host = ParseRequest.getIpAddr(request);
        logger.info("主机地址" + host);

        String contentType = request.getContentType();
        String method = request.getMethod();
        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/json";
        }
        if (isNeedMock(uri, host)) {
            response.setContentType(contentType);
            logger.info("该请求需要mock,当前主机地址" + host + "请求url" + url);
            return assemble.getRes(request, response, "/web-mock/distribute");
        } else {
            Map<String, String> map = appIPMap.get(uri.appName);
            if (map != null) {
                String str = ProcessBody.getRequestBody(request);
                String targetHost = map.get(host);
                if (StringUtils.isEmpty(targetHost)) {
                    logger.info("配置中未找到转发主机信息，故走mock逻辑");
                    return assemble.getRes(request, response, "/web-mock/distribute");
                }
                url = "http://" + targetHost + url;
                logger.info("该请求无需mock,当前主机地址：" + host + "，  转发请求的地址" + url);
                response.setContentType(contentType);

                return JavaProxy.proxyCall(url, method, str, contentType);
            }
            return null;
        }

    }


    public boolean isNeedMock(URI uri, String host) {
        if (interMap == null || StringUtils.isEmpty(interMap.get(uri.interfaceName))) {
            return false;
        }
        if (appIPMap == null || StringUtils.isEmpty(appIPMap.get(uri.appName).get(host))) {
            return false;
        }
        return true;
    }

    public boolean hasIPMap(String appName, String ip) {
        Map<String, String> map = appIPMap.get(appName);
        if (!StringUtils.isEmpty(getMapIP(map, ip))) {
            return true;
        }
        return false;
    }


    public String getMapIP(Map<String, String> ipMap, String ip) {
        return ipMap.get(ip);
    }

    public URI parseUri(String url) {
        URI uri = ProcessPath.getURL(url);
        return uri;
    }


    private void init() {
        try {
            String content = FileUtil.getFileContent(fullName);
            if (!StringUtils.isEmpty(content)) {
                fullDisConfig = JSONObject.parseObject(content, FullDisConfig.class);
                if (fullDisConfig != null) {
                    interMap = fullDisConfig.getInterMap();
                    appIPMap = fullDisConfig.getAppIPMap();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("获取配置文件异常");
        }

        if (interMap == null) {
            interMap = new HashMap<>();
        }
        if (appIPMap == null) {
            appIPMap = new HashMap<>();
        }
    }


}
