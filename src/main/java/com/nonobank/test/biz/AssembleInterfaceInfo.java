package com.nonobank.test.biz;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.api.Mock;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.MockInterInfo;
import com.nonobank.test.entity.URI;
import com.nonobank.test.utils.FileResource;
import com.nonobank.test.utils.PathUtil;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 处理SetInterface接口内容，将其转换为接口信息
 * Created by H.W. on 2018/4/13.
 */


@Component
public class AssembleInterfaceInfo {
    @Value("${prePath}")
    String prePath;

    @Autowired
    PathUtil pathUtil;


    private static Logger logger = LoggerFactory.getLogger(AssembleInterfaceInfo.class);


    public MockInterInfo getInterfaceInfo(String json) throws MockException {
        if (json == null) {
            throw new MockException("setInterface请求内容不能为空");
        }
        JSONObject obj = JSONObject.parseObject(json);
        parsePathInfo(obj);
        setFieldValue(obj, "request");
        setFieldValue(obj, "response");
        MockInterInfo mockInterInfo = JSONObject.parseObject(obj.toJSONString(), MockInterInfo.class);
        if (mockInterInfo == null) {
            throw new MockException("setInterface请求内容转换为InterfaceInfo实例失败");
        }
        return mockInterInfo;
    }


    /**
     * 解析请求中request、response值
     *
     * @param obj
     * @param str
     * @throws MockException
     */
    private void setFieldValue(JSONObject obj, String str) throws MockException {
        String request = obj.getString(str);
        if (str.equalsIgnoreCase("config") && request == null) {
            return;
        }
        if (StringUtils.isEmpty(request)) {
            throw new MockException(str + "字段必须存在且不能为空");
        }
        if ("GET".equalsIgnoreCase(obj.getString("methodType")) && "request".equalsIgnoreCase(str)) {
            obj.put("request", ProcessText.fromGetStr(request));
        } else {
            if (request.startsWith("<")) {
                obj.put(str, ProcessText.fromXMlStr(request));
            } else if (request.startsWith("{")) {
                obj.put(str, ProcessText.fromJsonStr(request));
            } else {
                throw new MockException(str + "的值必须以 < 或 { 开头");
            }
        }
    }

    /**
     * 请求中url值解析为对应field
     *
     * @param jsonObject
     * @throws MockException
     */
    private void parsePathInfo(JSONObject jsonObject) throws MockException {
        String url = jsonObject.getString("url");
        if (StringUtils.isEmpty(url)) {
            throw new MockException("url字段必须存在且不能为空");
        }
        URI uri = ProcessPath.getURI(url);
        jsonObject.put("env", uri.env);
        jsonObject.put("appName", uri.appName);
        jsonObject.put("interfaceName", uri.interfaceName);
    }

    public MockInterInfo setInterfaceConfig(String json) throws MockException {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String relative = pathUtil.getRelativePath(jsonObject.getString("url"));
        String filePath = pathUtil.getFilePath(relative);
        String str = FileResource.getResource(filePath);
        if (str==null){
            throw new MockException("为找到符合路径"+filePath+"的资源");
        }
        JSONObject jsonObject1 = JSONObject.parseObject(str);
        jsonObject1.putAll(jsonObject);
        return JSONObject.parseObject(jsonObject1.toJSONString(), MockInterInfo.class);
    }
}
