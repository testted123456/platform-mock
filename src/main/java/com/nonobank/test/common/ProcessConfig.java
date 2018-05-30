package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.Config;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.utils.StringUtils;
import j.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.jvm.hotspot.oops.ObjArrayKlass;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by H.W. on 2018/4/25.
 */


public class ProcessConfig {
    private static Logger logger = LoggerFactory.getLogger(ProcessConfig.class);


    private static void delayTime(String delayTime) throws MockException {
        try {
            if (StringUtils.isEmpty(delayTime)) {
                return;
            }
            int time = Integer.valueOf(delayTime);
            if (time > 0) {
                Thread.sleep(time * 100);
                logger.info("响应延迟" + delayTime + "秒");
                return;
            }
            throw new MockException("Config中delayTime需正整数，当前为" + delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new MockException("Config中delayTime需正整数，当前为" + delayTime);
        }

    }

    private static void httpCode(HttpServletResponse res, String httpCode) throws MockException {
        if (StringUtils.isEmpty(httpCode)) {
            return;
        }
        try {
            int code = Integer.valueOf(httpCode);
            res.setStatus(code);
            logger.info("设置响应httpcode为" + httpCode);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new MockException("Config的httpcode需是标准httpCode，当前为:" + httpCode);
        }

    }

    private static void errorRate(HttpServletResponse res, String errRate) throws MockException {
        if (StringUtils.isEmpty(errRate)) {
            return;
        }
        double d = Double.valueOf(errRate);
        if (!(d >= 0 && d < 1)) {
            throw new MockException("errRate值需在0~1之间，当前为:" + errRate);
        }
        double temp = Math.random();
        if (d > temp) {
            res.setStatus(400);
        }
    }


    public static void process(Map<String, Object> configs, HttpServletResponse res) throws MockException {
        if (configs != null && !configs.isEmpty()) {
            for (Map.Entry<String, Object> config : configs.entrySet()) {
                String key = config.getKey();
                if (key.equalsIgnoreCase("delayTime")) {
                    delayTime(String.valueOf(config.getValue()));
                }
                if (key.equalsIgnoreCase("errRate")) {
                    errorRate(res, String.valueOf(config.getValue()));
                }
                if (key.equalsIgnoreCase("httpCode")) {
                    httpCode(res, String.valueOf(config.getValue()));
                }
                if (key.equalsIgnoreCase("ipMap")) {
                    Map<String, Object> ipMap = JSONObject.parseObject((String) config.getValue());
                    ProccessDistribute.setIpMap(ipMap);
                }
            }
        }
    }
}
