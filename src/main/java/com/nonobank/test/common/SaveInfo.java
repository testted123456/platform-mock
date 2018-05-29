package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.Config;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.FileUtil;
import com.nonobank.test.utils.FormatUtil;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by H.W. on 2018/4/25.
 */

@Component
public class SaveInfo {
    private static Logger logger = LoggerFactory.getLogger(SaveInfo.class);

    private String relativeFilePath = null;

    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;

    @Autowired
    private PathInfoServiceImpl pathInfoService;
/*
    @Autowired
    private ThirdpartyServiceImpl thirdpartyService;*/

    public boolean writeDB(MockInterfaceInfo interfaceInfo) throws MockException {
        logger.info("存储到数据库：" + interfaceInfo.getUrl());
        MockInterfaceInfo temp = interfaceInfoService.add(interfaceInfo);
        if (temp != null) {
            return true;
        }
        logger.error(interfaceInfo.getUrl() + "写入数据库失败");
        return false;

    }

    public boolean writeDB(Long interfaceInfoId, List<Config> configs) throws MockException {
        MockInterfaceInfo mockInterfaceInfo = interfaceInfoService.getMockInterfaceInfoById(interfaceInfoId);
        if (mockInterfaceInfo == null) {
            throw new MockException("不存在id为:" + interfaceInfoId + "对应的接口，请检查");
        }
        String confStr = JSONObject.toJSONString(configs);
        mockInterfaceInfo.setConfig(confStr);
        if (interfaceInfoService.add(mockInterfaceInfo) != null) {
            return true;
        }

        logger.error(mockInterfaceInfo.getUrl() + "更新配置" + confStr + "失败");
        return false;
    }

    public boolean writeInterfaceInfo(String prefixPath, MockInterfaceInfo interfaceInfo) throws MockException {
        String fullName = prefixPath + relativeFilePath;
        logger.info("写入文件的全路径: " + fullName);
        try {
            String content = JSONObject.toJSONString(interfaceInfo);
            return FileUtil.createFile(fullName, FormatUtil.formatJson(content));
        } catch (IOException e) {
            e.printStackTrace();
            throw new MockException("写入mock接口数据到文件系统异常，异常信息: " + e.getMessage());
        }
    }

    public boolean writeConfig(String json, String prefixPath) throws MockException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        String config = jsonObject.getString("config");
        if (type.equalsIgnoreCase("0")) {
         /*   Thirdparty thirdparty = thirdpartyService.findThirdpartyByName(name);
            if (thirdparty == null) {
                throw new MockException("应用" + name + "不存在");
            }
            thirdparty.setConfig(config);
            return thirdpartyService.add(thirdparty) == null ? false : true;*/

        }
        MockInterfaceInfo infoByUrl = interfaceInfoService.getMockInterfaceInfoByUrl(name);
       /* convertValue(jsonObject, "url");
        String fullName = prefixPath + relativeFilePath;
        String content = FileUtil.getFileContent(fullName);*/
        if (infoByUrl == null) {
            throw new MockException("接口" + name + "不存在");
        }
        infoByUrl.setConfig(config);
        return interfaceInfoService.add(infoByUrl) == null ? false : true;
    }

    public MockInterfaceInfo str2InterfaceInfo(JSONObject jsonObject) throws MockException {

        convertValue(jsonObject, "url");
        convertValue(jsonObject, "request");
        convertValue(jsonObject, "response");
        convertValue(jsonObject, "config");
        convertValue(jsonObject,"resType");
        return JSONObject.parseObject(jsonObject.toJSONString(), MockInterfaceInfo.class);
    }


    private void convertValue(JSONObject jsonObject, String key) throws MockException {
        String str = jsonObject.getString(key);
        if (StringUtils.isEmpty(str) && !key.equalsIgnoreCase("config")) {
            throw new MockException(key + "值不能为空");
        }
        if ("url".equalsIgnoreCase(key)) {
            String url = jsonObject.getString(key);
            URI uri = ProcessPath.getURL(url);
            if (uri == null) {
                throw new MockException("url值不符合规则，当前值为: " + str);
            }
            relativeFilePath = uri.toString();
            jsonObject.putAll(uri.map);
            return;
        }
        if ("config".equalsIgnoreCase(key)) {
            if (StringUtils.isEmpty(str)) {
                return;
            }
            String json = ProcessText.getSpecTypeStr(str, Type.JSON);
            jsonObject.put(key, JSONObject.parseObject(json));
            return;

        }
        if ("resType".equalsIgnoreCase(key)){
            if (StringUtils.isEmpty(str)){
                jsonObject.put(key,0);
                return;
            }else {
                if (str.toLowerCase().contains("xml")){
                    jsonObject.put(key,1);
                }
                jsonObject.put(key,0);
                return;
            }

        }
        if ("request".equalsIgnoreCase(key) && "GET".equalsIgnoreCase(jsonObject.getString("methodType"))) {
            jsonObject.put(key, new JSONObject(ProcessText.fromGetType(str)));
            return;
        } else {
            String json = ProcessText.getSpecTypeStr(str, Type.JSON);
            jsonObject.put(key, JSONObject.parseObject(json));
            return;
        }

    }

    public PathInfo writePathInfo2DB(String pathName, Long pid,String config) throws MockException {
        PathInfo prePathInfo = pathInfoService.getPathInfoById(pid);
        if (prePathInfo == null) {
            throw new MockException("pid不存在");
        }
        PathInfo newPath = new PathInfo();
        newPath.setPathName(pathName);
        newPath.setPid(pid);
        newPath.setFullName(prePathInfo.getFullName() + "/" + pathName);
        newPath.setPreviousName(prePathInfo.getPathName());
        newPath.setConfig(config);
        return pathInfoService.add(newPath) ;
    }
}
