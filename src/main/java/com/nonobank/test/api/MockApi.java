package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.common.ProcessBody;
import com.nonobank.test.common.SaveInfo;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.InterfaceInfo;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
@RestController
@RequestMapping(value = "/mock")
public class MockApi {
    private static Logger logger = LoggerFactory.getLogger(MockApi.class);


    @Value("${prefixPath}")
    private String prefixPath;
    @Autowired
    private SaveInfo saveInfo;

    @RequestMapping(value = "/setInterface", method = RequestMethod.POST)
    public ResponseResult<String> writeInterface(@RequestBody String body) {
        logger.info("准备写入接口信息" + body);
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = saveInfo.str2InterfaceInfo(body);
            saveInfo.writeInterfaceInfo(prefixPath, interfaceInfo);
        } catch (MockException e) {
            e.printStackTrace();
            return ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());
        }
        logger.info("写入接口信息完成" + interfaceInfo.getUrl());

        return ResponseResult.success(Code.Res.SUCCESS, interfaceInfo.getUrl() + "接口mock创建成功");
    }

    @RequestMapping(value = "/getNodePath", method = RequestMethod.POST)
    public ResponseResult<String> getNodePath(@RequestBody String json) {
        Map<String,Object> target = new HashMap<>();
        try {
            ProcessBody.getOneDepthMap(json,target);
        } catch (MockException e) {
            e.printStackTrace();
            return ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());
        }
        return ResponseResult.success(Code.Res.SUCCESS, JSONObject.toJSONString(target));
    }


    @RequestMapping(value = "/setConfig",method =  RequestMethod.POST)
    public ResponseResult<String> setConfig(@RequestBody String json) {
        logger.info("设置配置请求"+json);
        try {
            saveInfo.writeConfig(json,prefixPath);
        } catch (MockException |IOException e) {
            e.printStackTrace();
            return ResponseResult.error(Code.Res.UNKNOWN_ERROR,e.getMessage());
        }
        logger.info("配置设置成功");
        return ResponseResult.success(Code.Res.SUCCESS,"配置设置成功");
    }
}
