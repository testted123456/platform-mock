package com.nonobank.test.common;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.DBResource.entity.*;
import com.nonobank.test.DBResource.service.impl.InterfaceInfoServiceImpl;
import com.nonobank.test.DBResource.service.impl.PathInfoServiceImpl;
import com.nonobank.test.DBResource.entity.MockException;
import com.nonobank.test.DBResource.entity.ValidException;
import com.nonobank.test.utils.ArgsValid;
import com.nonobank.test.utils.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by H.W. on 2018/5/28.
 */

@Service
public class AcquireInfo {

    private Logger logger = LoggerFactory.getLogger(AcquireInfo.class);

    @Autowired
    private InterfaceInfoServiceImpl interfaceInfoService;
    @Autowired
    private PathInfoServiceImpl pathInfoService;
    @Autowired
    private PreserveConfig preserveConfig;
    @Autowired
    private ForwardRequest forwardRequest;

    @Autowired
    private ParseHttpServletRequest parseHttpServletRequest;

    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";


    public String getNodeList(String id) {
        ArgsValid.notEmpty(id, "节点Id");
        Long nodeId = Long.valueOf(id);
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setId(nodeId);
        baseInfo.setDirectory(true);
        List<PathInfo> paths = pathInfoService.findByPid(nodeId);
        List<MockInterfaceInfo> mocks = interfaceInfoService.findByPathInfoId(nodeId);
        List<BaseInfo> infos = new ArrayList<>();
        infos.addAll(paths.parallelStream().map(
                pathInfo -> {
                    pathInfo.setDirectory(true);
                    return pathInfo;
                }
        ).collect(Collectors.toList()));
        infos.addAll(mocks.parallelStream().map(
                interfaceInfo -> {
                    interfaceInfo.setDirectory(false);
                    return interfaceInfo;
                }
        ).collect(Collectors.toList()));

        baseInfo.getList().addAll(infos);
        return JSONObject.toJSONString(baseInfo);
    }


    public Map<String, Object> transToSimpleMap(HttpServletRequest request, Map<String, Object> target) {
        Map<String, Object> map = parseHttpServletRequest.getRequestBody(request);
        Convert.sourceMapToSimpleMap(map, target, "");
        return target;
    }

    public String getInterfaceInfo(String id) {
        ArgsValid.notEmpty(id, "接口id");
        MockInterfaceInfo info = interfaceInfoService.getMockInterfaceInfoById(Long.valueOf(id));
        return JSONObject.toJSONString(info);
    }

    public String getResponse(HttpServletRequest request, HttpServletResponse response) throws MockException, IOException {
        String mockFullName = request.getRequestURI().replaceFirst("/web-mock", "");
        ArgsValid.notEmpty(mockFullName, "接口全路径");
        MockInterfaceInfo info = interfaceInfoService.getMockInterfaceInfoByUrl(mockFullName);
        if (info == null) {
            logger.info("接口" + mockFullName + "未查找到,返回请求消息体");
            return parseHttpServletRequest.getResponse(request);
        }

        Map<String, Object> configs = preserveConfig.getInterfaceInfoConfig(mockFullName);
        ProcessConfig.process(configs, response);
        if (info.getNeedProxy()) {
            String forwardResponse = forwardRequest.forwardRequest(request, response);
            if (!forwardResponse.equals("-1")) {
                return forwardResponse;
            } else {
                throw new ValidException("未找到转发主机信息,请检查配置");
            }
        }

        String resStr = info.getResponse();
        JSONObject responseMap = JSONObject.parseObject(resStr);

        fillResponseByRequestData(responseMap, parseHttpServletRequest.getRequestBody(request));

        if (needXmlResponse(info.getResType())) {
            response.setContentType("application/xml");
            return xmlResponse(responseMap);
        }
        response.setContentType("application/json");

        return JSONObject.toJSONString(responseMap);
    }

    private void fillResponseByRequestData(JSONObject responseMap, JSONObject requestMap) {
        Map<String, Object> simpleMap = new HashMap<>();
        Convert.sourceMapToSimpleMap(requestMap, simpleMap, "");
        traversal(responseMap, simpleMap);
    }

    private boolean needXmlResponse(int i) {
        return i == 1;
    }

    private String xmlResponse(JSONObject responseMap) {
        String response = JSONObject.toJSONString(responseMap);
        response = parseHttpServletRequest.getSpecTypeStr(response, "xml");
        return XML_HEAD + "\n" + response;

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






