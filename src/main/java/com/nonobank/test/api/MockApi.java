package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.biz.AssembleResponse;
import com.nonobank.test.biz.ProcessConfig;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.Config;
import com.nonobank.test.entity.ResponseResult;
import com.nonobank.test.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by H.W. on 2018/4/12.
 */

@RestController
public class MockApi {
    private static Logger logger = LoggerFactory.getLogger(MockApi.class);

    @Autowired
    ProcessConfig processConfig;
    @Autowired
    Config config;
    @Autowired
    AssembleResponse assembleResponse;
    private String env = "stb";
    private String appName = null;
    private String interName = null;


    @RequestMapping(value = "/**")
    public String assembleRes(HttpServletRequest servletRequest, HttpServletResponse response) {
        String res = null;
        String message = null;
        int delayTime = 0;

        try {
            if (!config.isEmpty()) {
                processConfig.process(config, response);
            }
            res = assembleResponse.getMockResponse(servletRequest, response);

        } catch (MockException e) {
            e.printStackTrace();
            String str = JSONObject.toJSONString(ResponseResult.error(Code.Res.UNKNOWN_ERROR, e.getMessage()));
            return str;
        }
        return res;
    }

/*
    public String getResponse(HttpServletRequest servletRequest) {
        String result = null;
        String reqBody = null;
        String str = servletRequest.getRequestURI();
        String reqType = servletRequest.getMethod();
        if ("GET".equalsIgnoreCase(reqType)) {
            XMap xMap = new XMap(servletRequest.getParameterMap());
            reqBody = xMap.toJSON();
        } else if ("POST".equalsIgnoreCase(reqType)) {
            reqBody =assembleResponse.getBodyData(servletRequest);
        }
        String[] strings = PathUtil.splitBySlash(str);
        if ("stb".equalsIgnoreCase(strings[0]) || "sit".equalsIgnoreCase(strings[0])) {
            env = strings[0];
            setMockInterinfo(strings[1]);
        } else {
            appName = strings[0];
            interName = strings[1];
        }
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(interName)) {
            ResponseResult result1 = ResponseResult.error(Code.Res.VALID_ERROR, "未能解析到thirdName或interfaceName");
            return JSONObject.toJSONString(result1);
        }

        String path = FileResource.getDirpath(env, appName, interName);
        result = getResStrByPath(path, reqBody);
        if (result == null) {
            String originPath = FileResource.getDirpath("", appName, interName);
            result = getResStrByPath(originPath, reqBody);
            if (result == null) {
                return reqBody;
            } else {
                try {
                    copyFile(env,appName, interName);
                } catch (MockException e) {
                    e.printStackTrace();
                    logger.info("复制文件" + originPath + "失败");
                    return result;

                }
                return result;
            }
        } else {
            return result;
        }
    }*/

/*
    private void setMockInterinfo(String string) {
        try {
            TwoTuple<String,String> tuple = PathUtil.getInterInfo(string);
            appName = tuple.t;
            interName = tuple.v;
        } catch (MockException e) {
            e.printStackTrace();
        }

    }*/

    /* private String getBodyData(HttpServletRequest request) {
         StringBuffer data = new StringBuffer();
         String line = null;
         BufferedReader reader = null;
         try {
             reader = request.getReader();
             while (null != (line = reader.readLine()))
                 data.append(line);
         } catch (IOException e) {
         } finally {
         }
         return data.toString();
     }
 */
    private String getResStrByPath(String path, String reqBody) {
        String result = null;
        try {
            result = FileResource.getResource(path);
            if (result != null) {
                //   MockInterInfo interfaceInfo = convert.transInterface(result,false);
                //  String string = extract.getResponse(reqBody, interfaceInfo.getResponse().toJSONString(), interfaceInfo.getResType());
                return "";
            }
        } catch (MockException e) {
            e.printStackTrace();
            ResponseResult result1 = ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());

            return JSONObject.toJSONString(result1);

        }
        return result;
    }


}



