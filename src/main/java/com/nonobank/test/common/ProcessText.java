package com.nonobank.test.common;

import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Type;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
public class ProcessText {

    private static Logger logger = LoggerFactory.getLogger(ProcessText.class);



    /**
     * 处理get请求时的字符串，形如"id=1&name=calder"
     *
     * @param str
     * @return
     */
    public static Map<String,Object> fromGetType(String str) {
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
        return map;
    }


    public static String getSpecTypeStr(String str,Type type) throws MockException {
        JSONObject jsonObject = null;
        if (str.startsWith("{")){
            jsonObject = new JSONObject(str);
        }else if (str.startsWith("<")){
            jsonObject = XML.toJSONObject(str);
        }else {
            throw new MockException("只支持json或xml格式，字串需以'{'或'<'开头");
        }
        return getSpecTypeStr(jsonObject,type);
    }


    private static String getSpecTypeStr(JSONObject jsonObject, Type type){
        switch (type.getId()){
            case 1://xml
               return XML.toString(jsonObject,null);
            case 2:
            default://json
               return jsonObject.toString();
        }
    }

}
