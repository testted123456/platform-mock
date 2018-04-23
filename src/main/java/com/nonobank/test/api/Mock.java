package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.biz.AssembleInterfaceInfo;
import com.nonobank.test.biz.ProcessText;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.MockInterInfo;
import com.nonobank.test.entity.ResponseResult;
import com.nonobank.test.utils.FileResource;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Created by H.W. on 2018/4/11.
 */

@RestController
@RequestMapping(value = "/mock")
public class Mock {

    private Logger logger = LoggerFactory.getLogger(Mock.class);

    @Autowired
    AssembleInterfaceInfo assembleInterfaceInfo;
    @Value("${prePath}")
    String prefixPath;

    /**
     * @param {String} url  被mock接口请求地址，例:/usr-app/auth/getUserId,usr-app即mock模拟的第三方服务名，剩余部分为接口名
     * @param {String} request  被mock接口入参
     * @param {String} response  被mock接口响应
     * @param {String} desc       被mock接口功能描述
     * @param {String} [resType] 可选,响应返回类型，默认为json(功能暂未实现)
     * @return
     * @api {POST} /mock/setInterface   新增mock接口
     * @apiGroup mock
     * @apiParamExample {json} Request-Example
     * {
     * "url":"/usr-app/auth/getUserId",
     * "request":"{"userid":"123213213",
     * "realname":"刘宁",
     * "idnum":"120107197807313915",
     * "authType":4,
     * "channelType":"2"
     * }"
     * <p>
     * "response":"{
     * "succeed": true,
     * "errorMessage": "",
     * "errorCode": "0000000",
     * "data": null
     * }"
     * }
     */
    @RequestMapping(value = "/setInterface", method = RequestMethod.POST)
    public ResponseResult<String> operateInterface(@RequestBody String json) {
        logger.info("设置新mock接口,request内容" + json);
        String result = null;
        MockInterInfo mockInterInfo = null;
        if (StringUtils.isEmpty(json)) {
            return ResponseResult.error(Code.Res.PARAMETER_VALID_ERROR, "请求消息体不能为空");
        }

        try {
            mockInterInfo = assembleInterfaceInfo.getInterfaceInfo(json);
            result = JSONObject.toJSONString(mockInterInfo);
        } catch (MockException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());
        }

        try {
            FileResource.createFile(prefixPath,"", mockInterInfo.getAppName(), mockInterInfo.getInterfaceName(), result);
        } catch (MockException e) {
            e.printStackTrace();
            return ResponseResult.error(Code.Res.UNKNOWN_ERROR, e.getMessage());

        }

        return ResponseResult.success("success", mockInterInfo.getInterfaceName() + "文件创建成功");
    }


    /**
     * @param {String} request 被mock接口请求入参
     * @return
     * @api {POST} /mock/getNodePath 将request转换为深度为1的Map，用于构造request
     * @apiGroup mock
     */
    @RequestMapping(value = "/getNodePath", method = RequestMethod.POST)
    public ResponseResult<String> getNodePath(@RequestBody String json) {
        ResponseResult<String> result = null;
        String request = json;
        if (StringUtils.isEmpty(request)) {
            return ResponseResult.error(Code.Res.PARAMETER_VALID_ERROR, "request不能为空");
        }
        try {
            String data = JSONObject.toJSONString(ProcessText.requst2Map(request));
            result = ResponseResult.success("success", data);
            logger.info(data);
        } catch (MockException e) {
            return ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/setConfig",method = RequestMethod.POST)
    public ResponseResult<String> setConfig(@RequestBody String request)  {
       String result = null;
        MockInterInfo mockInterInfo = null;
        try {
            mockInterInfo = assembleInterfaceInfo.setInterfaceConfig(request);
        } catch (MockException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseResult.error(Code.Res.VALID_ERROR, e.getMessage());
        }
         result = JSONObject.toJSONString(mockInterInfo);

        try {
            FileResource.createFile(prefixPath,mockInterInfo.getEnv(), mockInterInfo.getAppName(), mockInterInfo.getInterfaceName(), result);
        } catch (MockException e) {
            e.printStackTrace();
            return ResponseResult.error(Code.Res.UNKNOWN_ERROR, e.getMessage());
        }

        return ResponseResult.success("success", mockInterInfo.getInterfaceName() + "Config保存成功，对应环境："+mockInterInfo.getEnv());
    }


    }
