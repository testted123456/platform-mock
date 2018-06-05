package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.ValidException;
import org.json.XML;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by H.W. on 2018/5/31.
 */

@Component
public class ParseHttpServletRequest {


    private boolean needXmlString(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType.toLowerCase().contains("xml");
    }

    public JSONObject getRequestBody(HttpServletRequest request) {
        String method = request.getMethod();
        String body = null;
        if (method.equalsIgnoreCase("GET")) {
            body = _getBody(request);
        } else if (method.equalsIgnoreCase("POST")) {
            body = _postBody(request);
        } else {
            throw new ValidException("只支持GET、POST请求");
        }
        return JSONObject.parseObject(body);
    }

    public String getResponse(HttpServletRequest request) {
        String body = JSONObject.toJSONString(getRequestBody(request));
        if (needXmlString(request)) {
            return getSpecTypeStr(body, "xml");
        }
        return body;
    }

    public  String getSpecTypeStr(String str, String type) {
        org.json.JSONObject jsonObject = null;
        if (str.startsWith("{")) {
            jsonObject = new org.json.JSONObject(str);
        } else if (str.startsWith("<")) {
            jsonObject = XML.toJSONObject(str);
        } else {
            throw new ValidException("只支持json或xml格式，字串需以'{'或'<'开头");
        }
        return getSpecTypeStr(jsonObject, type);
    }


    private  String getSpecTypeStr(org.json.JSONObject jsonObject, String type) {
        if (type.toLowerCase().equalsIgnoreCase("xml")) {
            return XML.toString(jsonObject, null);
        } else {
            return jsonObject.toString();
        }
    }

    private String _getBody(HttpServletRequest request) {
        return JSONObject.toJSONString(request.getParameterMap());
    }

    private String _postBody(HttpServletRequest request) {
        return getBodyString(request);
    }


    private String getBodyString(HttpServletRequest request) {
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
