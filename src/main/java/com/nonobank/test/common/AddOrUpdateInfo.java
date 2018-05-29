package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Type;
import com.nonobank.test.entity.URI;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/5/29.
 */
@Service
public class AddOrUpdateInfo {
    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;

    @Autowired
    private PathInfoServiceImpl pathInfoService;



    public void operate(JSONObject  jsonObject) throws MockException {

        String pid = jsonObject.getString("pid");
        ArgsValid.notEmpty(pid, "父节点id");

        Map<String,Integer> map = new HashMap();
        map.put("id",Integer.valueOf(pid));
        jsonObject.put("pathInfo", map);

        convertValue(jsonObject, "request");
        convertValue(jsonObject, "response");
        convertValue(jsonObject, "config");
        convertValue(jsonObject,"resType");
        String json = jsonObject.toJSONString();
        MockInterfaceInfo info = JSONObject.parseObject(json, MockInterfaceInfo.class);
        valid(info);
        interfaceInfoService.add(info);
    }
    public void operate(PathInfo info) throws MockException {
        valid(info);
        PathInfo prePathInfo = pathInfoService.getPathInfoById(info.getPid());
        if (prePathInfo == null) {
            throw new MockException("pid不存在");
        }
        info.setFullName(prePathInfo.getFullName() + "/" + info.getPathName());
        info.setPreviousName(prePathInfo.getPathName());
        pathInfoService.add(info);

    }




    private void valid(MockInterfaceInfo info){
        ArgsValid.notNull(info,"请求");
        ArgsValid.notEmpty(info.getName(),"接口名");
        ArgsValid.notNull(info.getPathInfo(),"父节点");
        ArgsValid.notEmpty(info.getUrl(),"接口全路径");
        ArgsValid.notEmpty(info.getRequest(),"请求消息体");
        ArgsValid.notEmpty(info.getRequest(),"响应消息体");
    }

    private void valid(PathInfo info){
        ArgsValid.notNull(info,"请求");
        ArgsValid.notEmpty(info.getPathName(),"目录名称");
        ArgsValid.notEmpty(String.valueOf(info.getPid()),"父节点id");
    }


    private void convertValue(JSONObject jsonObject, String key) throws MockException {
        String str = jsonObject.getString(key);
        if (StringUtils.isEmpty(str) && !key.equalsIgnoreCase("config")) {
            throw new MockException(key + "值不能为空");
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
}
