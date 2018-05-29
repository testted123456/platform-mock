package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.common.AcquireInfo;
import com.nonobank.test.common.AddOrUpdateInfo;
import com.nonobank.test.common.ProcessBody;
import com.nonobank.test.common.SaveInfo;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Result;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
@RestController
@RequestMapping(value = "/mock")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MockApi {
    private static Logger logger = LoggerFactory.getLogger(MockApi.class);


    @Value("${prefixPath}")
    private String prefixPath;
    @Autowired
    private SaveInfo saveInfo;
    @Autowired
    private AcquireInfo acquireInfo;

    @Autowired
    private AddOrUpdateInfo addOrUpdateInfo;


    @RequestMapping(value = "/setInterface", method = RequestMethod.POST)
    public Result writeInterface(@RequestBody String body) {
        logger.info("准备写入接口信息" + body);
        JSONObject jsonObject = JSONObject.parseObject(body);

        try {
            addOrUpdateInfo.operate(jsonObject);
        } catch (MockException e) {
            e.printStackTrace();
            Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), e.getMessage());
        }
        return Result.success("接口mock创建成功");
    }

    @RequestMapping(value = "/setPathInfo", method = RequestMethod.POST)
    public Result setPathInfo(@RequestBody String json) {
        logger.info("设置接口路径" + json);
        PathInfo pathInfo = JSONObject.parseObject(json, PathInfo.class);
        try {
            addOrUpdateInfo.operate(pathInfo);
        } catch (MockException e) {
            e.printStackTrace();
            return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), e.getMessage());
        }

        return Result.success(JSONObject.toJSONString(pathInfo));
    }


    @RequestMapping(value = "/getNodeList", method = RequestMethod.GET)
    public Result getNodeList(@RequestParam(value = "id", required = true) String id) {
        String nodeList = acquireInfo.getNodeList(id);
        return Result.success(nodeList);
    }

    @RequestMapping(value = "/getInterfaceInfo", method = RequestMethod.GET)
    public Result getInterfaceInfo(@RequestParam(value = "id", required = true) String id) {
        String infoStr = acquireInfo.getInterfaceInfo(id);
        return Result.success(infoStr);
    }

















    @RequestMapping(value = "/getNodePath", method = RequestMethod.POST)
    public Result getNodePath(@RequestBody String json) {
        Map<String, Object> target = new HashMap<>();
        try {
            ProcessBody.getOneDepthMap(json, target);
        } catch (MockException e) {
            e.printStackTrace();
            return Result.error(Code.ResultCode.VALIDATION_ERROR.getCode(), e.getMessage());
        }
        return Result.success(JSONObject.toJSONString(target));
    }


    @RequestMapping(value = "/setConfig", method = RequestMethod.POST)
    public Result setConfig(@RequestBody String json) {
        logger.info("设置配置请求" + json);
        try {
            saveInfo.writeConfig(json, prefixPath);
        } catch (MockException | IOException e) {
            e.printStackTrace();
            return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), e.getMessage());
        }
        logger.info("配置设置成功");
        return Result.success("配置设置成功");
    }




/*    @RequestMapping(value = "/setPathInfo", method = RequestMethod.POST)
    public Result setPathInfoOld(@RequestBody String json) {
        logger.info("设置接口路径" + json);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String pathName = jsonObject.getString("pathName");
        String pid = jsonObject.getString("pid");
        String config = jsonObject.getString("config");
        String isProxy = jsonObject.getString("isProxy");
        if (!(StringUtils.isEmpty(isProxy) || !"true".equalsIgnoreCase(isProxy))) {
            String ipMap = jsonObject.getString("ipMap");
        }
        if (StringUtils.isEmpty(pathName) ||
                StringUtils.isEmpty(pid)) {
            return Result.error(Code.ResultCode.VALIDATION_ERROR.getCode(), "当前目录名或上一级路径pid为空");
        }
        try {

            PathInfo pathInfo = saveInfo.writePathInfo2DB(pathName, Long.valueOf(pid), config);
            if (pathInfo != null) {
                return Result.success(JSONObject.toJSONString(pathInfo));
            } else {
                return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), "设置失败+" + json);
            }
        } catch (MockException e) {
            e.printStackTrace();
            return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), e.getMessage());
        }

    }*/

    @RequestMapping(value = "/setInterfaceOld", method = RequestMethod.POST)
    public Result writeInterfaceOld(@RequestBody String body) {
        logger.info("准备写入接口信息" + body);
        MockInterfaceInfo interfaceInfo = null;

        try {
            if (StringUtils.isEmpty(body)) {
                throw new MockException("请求内容不能为空");
            }
            JSONObject jsonObject = JSONObject.parseObject(body);

            interfaceInfo = saveInfo.str2InterfaceInfo(jsonObject);

            saveInfo.writeDB(interfaceInfo);

        } catch (MockException e) {
            e.printStackTrace();
            return Result.error(Code.ResultCode.VALIDATION_ERROR.getCode(), e.getMessage());
        }
        logger.info("写入接口信息完成" + interfaceInfo.getUrl());

        return Result.success(interfaceInfo.getUrl() + "接口mock创建成功");
    }

}
