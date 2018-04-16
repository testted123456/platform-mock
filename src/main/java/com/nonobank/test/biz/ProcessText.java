package com.nonobank.test.biz;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.utils.DataConvert;
import com.nonobank.test.utils.Extract;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理文本，返回标准map
 * Created by H.W. on 2018/4/13.
 */


public class ProcessText {


    private static Logger logger = LoggerFactory.getLogger(ProcessText.class);

    @Autowired
   static Extract extract;
    /**
     * 处理get请求时的字符串，形如"id=1&name=calder"
     * @param str
     * @return
     */
    public static JSONObject fromGetStr(String str){
        Map<String, Object> map = new HashMap<>();
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
        return new JSONObject(map);
    }

    /**
     * 将XMl格式文档转换为Map
     * @param str
     * @return
     */
    public static JSONObject fromXMlStr(String str){
        org.json.JSONObject jsonObject = XML.toJSONObject(str);

        return new JSONObject(jsonObject.toMap());
    }

    /**
     * 将JSON转换为Map
     * @param str
     * @return
     */
    public static JSONObject fromJsonStr(String str){
        return JSONObject.parseObject(str);
    }




    private static Map<String, Object> getMap(String str, String key) throws MockException {
        org.json.JSONObject jsonObject = DataConvert.getJSONObject(str);

        if (jsonObject.toMap().containsKey(key)) {
            jsonObject = jsonObject.getJSONObject(key);
        }
        Map<String, Object> map = jsonObject.toMap();
        return map;
    }

    public static Map<String, Object> requst2Map(String request) throws MockException {
        return extract.transMap(getMap(request, "request"));
    }
}
