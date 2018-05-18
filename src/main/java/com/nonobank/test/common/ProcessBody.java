package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Type;
import com.nonobank.test.utils.DataStruct;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/26.
 */
public class ProcessBody {

    private static Logger logger = LoggerFactory.getLogger(ProcessBody.class);
    //把请求响应的处理分解开




    public static void getOneDepthMap(String json,Map<String,Object> target) throws MockException {
        if (StringUtils.isEmpty(json)) {
            throw new MockException("请求内容不能为空");
        }
        Map<String, Object> map = null;
        if (json.contains("=")&&!(json.startsWith("{")||json.startsWith("<"))) {
            map = ProcessText.fromGetType(json);
        }else {
            json = ProcessText.getSpecTypeStr(json, Type.JSON);
            map = JSONObject.parseObject(json);
        }
        DataStruct.toOneDepth(map,target,"");
    }


    public static String getRequestBody(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }
}
