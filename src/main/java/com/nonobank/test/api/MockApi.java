package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.entity.PathInfo;
import com.nonobank.test.common.*;
import com.nonobank.test.DBResource.entity.Code;
import com.nonobank.test.DBResource.entity.Result;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/mock")
public class MockApi {
    private static Logger logger = LoggerFactory.getLogger(MockApi.class);


    @Value("${prefixPath}")
    private String prefixPath;
    /*    @Autowired
        private SaveInfo saveInfo;*/
    @Autowired
    private AcquireInfo acquireInfo;

    @Autowired
    private InterfaceOperate interfaceOperate;

    @Autowired
    private DirectoryOperate directoryOperate;
    /* @Autowired
     private NodeOperation nodeOperation;
 */
    @Autowired
    private PreserveConfig preserveConfig;


    @RequestMapping(value = "/setInterface", method = RequestMethod.POST)
    public Result writeInterface(@RequestBody String body) {
        logger.info("准备写入接口信息" + body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        MockInterfaceInfo info = interfaceOperate.operate(jsonObject);
        if (info != null) {
            return Result.success(JSONObject.toJSONString(info));
        }
        //nodeOperation.createMockInterface(jsonObject);
        return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), "接口设置失败");

    }

    @RequestMapping(value = "/setPathInfo", method = RequestMethod.POST)
    public Result setPathInfo(@RequestBody String json) {
        logger.info("设置目录" + json);
        PathInfo pathInfo = JSONObject.parseObject(json, PathInfo.class);

        boolean flag = directoryOperate.operate(pathInfo);
        if (!flag) {
            return Result.error(Code.ResultCode.UNKOWN_ERROR.getCode(), "目录设置失败");
        }


        return Result.success(JSONObject.toJSONString(pathInfo));
    }


    @RequestMapping(value = "/getNodeList", method = RequestMethod.GET)
    public Result getNodeList(@RequestParam(value = "id", required = true) String id) {
        String nodeList = acquireInfo.getNodeList(id);// acquireInfo(id);
        return Result.success(nodeList);
    }

    @RequestMapping(value = "/getInterfaceInfo", method = RequestMethod.GET)
    public Result getInterfaceInfo(@RequestParam(value = "id", required = true) String id) {
        String infoStr = acquireInfo.getInterfaceInfo(id);
        return Result.success(infoStr);
    }


    @RequestMapping(value = "/getConfigList", method = RequestMethod.GET)
    public Result getNodeConfig(@RequestParam(value = "type") String type) {
        boolean flag = false;
        if (!StringUtils.isEmpty(type)) {
            if (type.equalsIgnoreCase("dict")) {
                flag = true;
            }
        }
        String response = null;
        if (flag) {
            response = JSONObject.toJSONString(preserveConfig.getDictConfigList());
        } else {
            response = JSONObject.toJSONString(preserveConfig.getInterfaceConfigList());
        }

        //preserveBaseInfo.initConfig();
        return Result.success(response);
    }

    @RequestMapping(value = "/getRequestMap", method = RequestMethod.POST)
    public Result getNodePath(HttpServletRequest request) {
        Map<String, Object> target = new HashMap<>();
        target = acquireInfo.transToSimpleMap(request, target);
        return Result.success(JSONObject.toJSONString(target));
    }


   /* @RequestMapping(value = "/setConfig", method = RequestMethod.POST)
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

*/


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

  /*  @RequestMapping(value = "/setInterfaceOld", method = RequestMethod.POST)
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
    }*/

}
