package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.MockInterfaceInfo;
import com.nonobank.test.DBResource.service.InterfaceInfoService;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.entity.*;
import com.nonobank.test.utils.DataStruct;
import com.nonobank.test.utils.FileUtil;
import com.nonobank.test.utils.StringUtils;
import j.m.XMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
@Component
public class Assemble {

    private static Logger logger = LoggerFactory.getLogger(Assemble.class);
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    @Value("${prefixPath}")
    private String prefixPath;

    private String requestStr;
    private XMap reqParams;
    private String fullName;
    private String originalFile;
    private String contentType;


    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;


    public String getRes(HttpServletRequest request, HttpServletResponse response, String distribute) throws MockException {
      /*  String url = request.getRequestURI();
        parseServletRequest(request, distribute);
        InterfaceInfo interfaceInfo = interfaceInfoService.getMockInterfaceInfoByUrl();
        if (interfaceInfo == null) {
            response.setContentType(contentType);
            return requestStr;
        }
        String resType = interfaceInfo.getResType();
        Config config = interfaceInfo.getConfig();
        if (config != null) {
            ProcessConfig.process(config, response);
        }
        JSONObject resObj = interfaceInfo.getResponse();
        fillDataByReq(resObj);
        if ("xml".equalsIgnoreCase(resType)) {
            String str = JSONObject.toJSONString(resObj);
            str = ProcessText.getSpecTypeStr(str, Type.XML);
            response.setContentType("application/xml");
            return XML_HEAD + "\n" + str;
        }
        response.setContentType("application/json");

        return JSONObject.toJSONString(resObj);*/

      return null;


    }

    private InterfaceInfo getMockInterFromFile() throws MockException {
        try {
            String content = FileUtil.getFileContent(fullName);
            if (StringUtils.isEmpty(content)) { //如果请求路径下没有数据，到原始路径下查找
                if (StringUtils.isEmpty(originalFile)) {
                    return null;
                }

                content = FileUtil.getFileContent(originalFile);
                if (StringUtils.isEmpty(content)) {
                    return null;
                }
                FileUtil.copyFile(fullName, originalFile);
                logger.info("文件" + originalFile + System.lineSeparator() + "拷贝到" + fullName + System.lineSeparator() + "成功");
            }
            InterfaceInfo interfaceInfo = JSONObject.parseObject(content, InterfaceInfo.class);
            return interfaceInfo;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("文件" + originalFile + System.lineSeparator() + "拷贝到" + fullName + System.lineSeparator() + "失败");
            throw new MockException("文件: " + fullName + " 读取失败");
        }

    }

    private void parseServletRequest(HttpServletRequest request, String distribute) throws MockException {

        String url = request.getRequestURI();
        if (!StringUtils.isEmpty(distribute)) {
            url = url.replace(distribute, "");
        }
        fullName = getFullName(url);
        getReqParams(request);
         contentType = request.getContentType();
        if (contentType.toLowerCase().contains("xml")) {
            requestStr = ProcessText.getSpecTypeStr(requestStr, Type.XML);
        }
    }

    private String getFullName(String url) {
        URI uri = ProcessPath.getURL(url);
        String relative = "";
        if (uri != null) {
            relative = uri.toString();
        }

        fullName = prefixPath + relative;
        if (relative.startsWith(System.getProperty("file.separator") + "stb") || relative.startsWith(System.getProperty("file.separator") + "sit")) {
            originalFile = prefixPath + relative.substring(4);
        }
        return fullName;
    }

    private Map<String, Object> getReqParams(HttpServletRequest request) throws MockException {
        String type = request.getMethod();
        if (StringUtils.isEmpty(type) || "POST".equalsIgnoreCase(type)) {
            requestStr = ProcessBody.getRequestBody(request);
            if (!StringUtils.isEmpty(requestStr)) {
                String str = ProcessText.getSpecTypeStr(requestStr, Type.JSON);
                reqParams = XMap.fromJSON(str);
            }
        } else if ("GET".equalsIgnoreCase(type)) {
            reqParams = new XMap(request.getParameterMap());
            requestStr = reqParams.toPrettyJSON();
        } else {
            throw new MockException("尚不支持" + type + "类型请求");
        }
        return reqParams;
    }


    private void fillDataByReq(JSONObject resObj) {
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


    public static void main(String[] args) {
        System.out.println("/stb/exfs".substring(4));
    }
}
