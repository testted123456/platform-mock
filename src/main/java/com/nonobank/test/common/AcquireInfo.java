package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.*;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Type;
import com.nonobank.test.entity.ValidException;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.DataStruct;
import com.nonobank.test.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by H.W. on 2018/5/28.
 */

@Service
public class AcquireInfo {


    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;
    @Autowired
    private InitData initData;
    @Autowired
    private ProccessDistribute proccessDistribute;

    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";


    public String getInterfaceInfo(String pid) {
        ArgsValid.notEmpty(pid, "接口id");
        Long id = Long.valueOf(pid);
        MockInterfaceInfo info = interfaceInfoService.getMockInterfaceInfoById(id);
        return JSONObject.toJSONString(info);
    }

    public String getMockInterfaceInfo(HttpServletRequest request, HttpServletResponse response) throws MockException, IOException {
        String requsetBody = null;
        String urlStr = request.getRequestURI().replaceFirst("/web-mock", "");
        ArgsValid.notEmpty(urlStr, "接口全名");
        MockInterfaceInfo info = interfaceInfoService.getMockInterfaceInfoByUrl(urlStr);
        requsetBody = getRequestBody(request);
        if (info == null) {
            return requsetBody;
        }
        Map<String, Object> configs = initData.getInterfaceInfoConfig(urlStr);
        //undo 查看config的配置情况
        ProcessConfig.process(configs, response);
        if (info.getNeedProxy()) {
            String disposeRes = proccessDistribute.disposeRes(request, response);
            if (!disposeRes.equals("-1")) {
                return disposeRes;
            }
        }
        String resStr = info.getResponse();
        JSONObject resObj = JSONObject.parseObject(resStr);
        fillDataByReq(resObj, JSONObject.parseObject(requsetBody));
        //  if ("xml".equalsIgnoreCase(resType)) {
        if (info.getResType() == 1) {
            String str1 = JSONObject.toJSONString(resObj);
            str1 = ProcessText.getSpecTypeStr(str1, Type.XML);
            response.setContentType("application/xml");
            return XML_HEAD + "\n" + str1;
        }
        response.setContentType("application/json");

        return JSONObject.toJSONString(resObj);

        //todo response组装
    }


    public static String getRequestBody(HttpServletRequest request) {
        String result = null;
        String method = request.getMethod();
        if (!StringUtils.isEmpty(method) && "POST".equalsIgnoreCase(method)) {
            result = postReqBody(request);
        } else if (!StringUtils.isEmpty(method) && "GET".equalsIgnoreCase(method)) {
            result = JSONObject.toJSONString(request.getParameterMap());
        } else {
            throw new ValidException("请求不支持" + method + "类型");
        }
        return result;
    }

    private static String postReqBody(HttpServletRequest request) {
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


    private void fillDataByReq(JSONObject resObj, JSONObject reqParams) {
        Map<String, Object> oneDepth = new HashMap<>();
        DataStruct.toOneDepth(reqParams, oneDepth, "");
        traversal(resObj, oneDepth);
    }
    /**
     * 遍历response，构造响应数据
     *
     * @param map
     */
    private void traversal(Map<String, Object> map, Map<String, Object> oneDepth) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                traversal((Map<String, Object>) obj, oneDepth);
            } else if (obj instanceof List) {
                traversal((List) obj, oneDepth);
            } else {
                String tmpKey = "";
                if (obj instanceof Boolean || obj instanceof Integer ||
                        obj instanceof Character || obj instanceof Float || obj instanceof Double) {
                    tmpKey = String.valueOf(obj);
                } else {
                    tmpKey = (String) obj;
                }
                Object value = oneDepth.get(tmpKey);
                if (value != null && value.getClass().isArray()) {//
                    String[] strings = (String[]) value;
                    String temp = null;
                    for (int i = 0; i < strings.length; i++) {
                        if (i == 0) {
                            temp = strings[i];
                        } else {
                            temp = temp + "," + strings[i];
                        }
                    }
                    value = temp;
                }
                if (value != null) {
                    entry.setValue(value);
                }

            }
        }
    }

    /**
     * 遍历response，构造响应数据
     *
     * @param list
     */
    private void traversal(List<Object> list, Map<String, Object> oneDepth) {
        if (list == null) {
            return;
        }
        int length = list.size() > 0 ? list.size() : 0;
        for (int i = 0; i < length; i++) {
            Object obj = list.get(i);
            if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                traversal(map, oneDepth);
            } else {
                if (oneDepth.containsKey((String) obj)) {
                    Object value = oneDepth.get((String) obj);
                    if (value != null) {
                        list.set(i, value);
                    }
                }
            }
        }
    }

}






