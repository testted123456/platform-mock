package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.DataStruct;
import com.nonobank.test.utils.FileUtil;
import com.nonobank.test.utils.FormatUtil;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */

@Component
public class SaveInfo {
    private static Logger logger = LoggerFactory.getLogger(SaveInfo.class);

    private String relativeFilePath = null;

    public boolean writeInterfaceInfo(String prefixPath, InterfaceInfo interfaceInfo) throws MockException {
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

    public boolean writeConfig(String json,String prefixPath) throws MockException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(json);
        convertValue(jsonObject, "url");
        String fullName = prefixPath + relativeFilePath;
        String content =FileUtil.getFileContent(fullName);
        if (StringUtils.isEmpty(content)){
            throw new MockException("未找到 "+fullName+"对应文件");
        }
        InterfaceInfo interfaceInfo = JSONObject.parseObject(content,InterfaceInfo.class);
        Config config = jsonObject.getObject("config",Config.class);
        interfaceInfo.setConfig(config);
        return  writeInterfaceInfo(prefixPath,interfaceInfo);
    }

    public InterfaceInfo str2InterfaceInfo(String str) throws MockException {
        if (StringUtils.isEmpty(str)) {
            throw new MockException("请求内容不能为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(str);
        convertValue(jsonObject, "url");
        convertValue(jsonObject, "request");
        convertValue(jsonObject, "response");
        convertValue(jsonObject, "config");
        return JSONObject.parseObject(jsonObject.toJSONString(), InterfaceInfo.class);
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
        if ("request".equalsIgnoreCase(key) && "GET".equalsIgnoreCase(jsonObject.getString("methodType"))) {
            jsonObject.put(key, new JSONObject(ProcessText.fromGetType(str)));
            return;
        } else {
            String json = ProcessText.getSpecTypeStr(str, Type.JSON);
            jsonObject.put(key, JSONObject.parseObject(json));
            return;
        }
    }


}
