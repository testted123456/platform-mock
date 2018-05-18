package com.nonobank.test.api;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.common.ProccessDistribute;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/27.
 */

@RestController
@RequestMapping("/distribute")
public class Distribute {


    @Autowired
    ProccessDistribute proccessDistribute;

    @RequestMapping("/**")
    public void distributeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, MockException {
         String res = proccessDistribute.disposeRes(request, response);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(res);
         response.getWriter().flush();
        //response.setContentType("application/json");

    }


    @RequestMapping(value = "/setDisMockConfig", method = RequestMethod.POST)
    public ResponseResult<String> setDisMockConfig(@RequestBody String json) {
        if (StringUtils.isEmpty(json)) {
            return ResponseResult.error(Code.Res.VALID_ERROR, "请求参数不符合规则");
        }
        DisConfig disConfig = proccessDistribute.setConfig(json);
        if (disConfig == null) {
            return ResponseResult.error(Code.Res.UNKNOWN_ERROR, "设置失败");
        }
        return ResponseResult.success(Code.Res.SUCCESS, "设置成功");

    }


    @RequestMapping(value = "/delDisMockConfig", method = RequestMethod.POST)
    public ResponseResult<String> delDisMockConfig(@RequestBody String json) {
        if (StringUtils.isEmpty(json)) {
            return ResponseResult.error(Code.Res.VALID_ERROR, "请求参数不符合规则");
        }
        boolean bool = proccessDistribute.delConfig(json);
        if (bool) {
            return ResponseResult.success(Code.Res.SUCCESS, "设置成功");
        }
        return ResponseResult.error(Code.Res.UNKNOWN_ERROR, "设置失败");


    }
}
