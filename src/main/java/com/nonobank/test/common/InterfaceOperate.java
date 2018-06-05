package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/5/31.
 */
@Component
public class InterfaceOperate extends AbstractPreOperate {


    private Logger logger = LoggerFactory.getLogger(InterfaceOperate.class);
    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;


    public void validInterface(MockInterfaceInfo info) {
        valid(info);
        ArgsValid.notNull(info.getPathInfo(), "接口所属目录");
        ArgsValid.notEmpty(info.getUrl(), "interfaceUrl");
        ArgsValid.notEmpty(info.getRequest(), "ResquestBody");
        ArgsValid.notEmpty(info.getRequest(), "ResponseBody");
    }

    public MockInterfaceInfo convertInterface(JSONObject jsonObject) {
        String pid = jsonObject.getString("pid");
        ArgsValid.notEmpty(pid, "pid");
        setPathInfo(jsonObject, pid);
        convertFieldValue(jsonObject, "resType");
        convertFieldValue(jsonObject, "env");

        // convertFieldValue(jsonObject, "request");
        convertFieldValue(jsonObject, "response");
        convertFieldValue(jsonObject, "methodType");
        String infoStr = JSONObject.toJSONString(jsonObject);

        return JSONObject.parseObject(infoStr, MockInterfaceInfo.class);
    }

    private void convertFieldValue(JSONObject jsonObject, String key) {
        String value = jsonObject.getString(key);
        if ("resType".equalsIgnoreCase(key)) {
            jsonObject.put("resType", getDefaultValue(value, "xml"));
        } else if ("env".equalsIgnoreCase(key)) {
            jsonObject.put("env", getDefaultValue(value, "sit"));
        } else if ("methodType".equalsIgnoreCase(key)) {
            int i = getDefaultValue(value, "GET");
            if (i == 1) {
                String request = jsonObject.getString("request");
                if (!StringUtils.isEmpty(request) || !(request.startsWith("{") || request.startsWith("["))) {
                    request = JSONObject.toJSONString(getMapByGetBody(request));
                    jsonObject.put("request", request);
                }
            }
            jsonObject.put("methodType", i);
        } else if ("needProxy".equalsIgnoreCase(key)) {
            jsonObject.put("needProxy", Boolean.valueOf(value) ? 1 : 0);
        } else {
            ArgsValid.notEmpty(value, key);
        }
    }


    public Map<String, String> getMapByGetBody(String str) {
        Map<String, String> map = new HashMap<>();
        try {
            str = URLDecoder.decode(str, "UTF-8");
            String[] strings = str.split("&");
            for (int i = 0; i < strings.length; i++) {
                String[] param = strings[i].split("=");
                map.put(param[0], param[1]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("GET请求request解析失败" + "   " + e.getMessage());
            return null;
        }
        return map;
    }

    private int getDefaultValue(String value, String defaultValue) {
        return !StringUtils.isEmpty(value) && value.toLowerCase().contains(defaultValue) ? 1 : 0;
    }

    private void setPathInfo(JSONObject jsonObject, String id) {
        Map<String, Integer> map = new HashMap();
        map.put("id", Integer.valueOf(id));
        jsonObject.put("pathInfo", map);
    }

    public MockInterfaceInfo operate(JSONObject jsonObject) {
        MockInterfaceInfo info = convertInterface(jsonObject);
        valid(info);
        MockInterfaceInfo result = interfaceInfoService.add(info);
        return result;
    }
}
