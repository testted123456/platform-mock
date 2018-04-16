package com.nonobank.test.biz;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.MockInterInfo;
import com.nonobank.test.entity.URI;
import com.nonobank.test.utils.Extract;
import com.nonobank.test.utils.FileResource;
import com.nonobank.test.utils.DataConvert;
import com.nonobank.test.utils.StringUtils;
import j.m.XMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 构造返回数据
 * Created by H.W. on 2018/4/13.
 */
@Component
public class AssembleResponse {

    private static Logger logger = LoggerFactory.getLogger(AssembleResponse.class);

    @Autowired
    Extract extract;
    @Value("${prePath}")
    String prePath;

    private String requestURL;
    private String reqMethodType;
    private String contentType;
    private XMap reqParams;
    private String resDataType;
    private String filePath;
    private String originFilePath;
    private String reqStr;


    public String getMockResponse(HttpServletRequest servletRequest) throws MockException {
        String response = null;
        parseRequest(servletRequest);
        if (filePath != null) {
            MockInterInfo mockInterInfo = getInfoFromFile(filePath);
            if (mockInterInfo == null) {
                return reqStr;
            }
            JSONObject originRes = getOriginResponse(mockInterInfo);
            response = extract.getResponse(reqParams, originRes, resDataType == null ? "json" : resDataType);
        }

        return response;
    }
    
    /**
     * 解析请求数据
     *
     * @param servletRequest
     * @throws MockException
     */
    private void parseRequest(HttpServletRequest servletRequest) throws MockException {
        requestURL = servletRequest.getRequestURI();
        reqMethodType = servletRequest.getMethod();
        contentType = servletRequest.getContentType();
        if ("GET".equalsIgnoreCase(reqMethodType)) {
            reqParams = new XMap(servletRequest.getParameterMap());
        } else if ("POST".equalsIgnoreCase(reqMethodType)) {
            reqStr = getBodyData(servletRequest);
            reqParams = XMap.fromJSON(DataConvert.getJsonStr(reqStr));
        } else {
            throw new MockException("当前不支持contentType类型" + reqMethodType);
        }
        logger.info("请求接口为: "+requestURL+",请求参数为："+reqParams.toPrettyJSON());
        parseFilePath(requestURL);
    }

    private String parseFilePath(String requestURL) throws MockException {
        if (StringUtils.isEmpty(requestURL)) {
            throw new MockException("mock请求中接口路径不存在");
        }
        URI<String, String, String> uri = ProcessPath.getURI(requestURL);
        filePath = FileResource.getDirpath(prePath,uri.env, uri.appName, uri.interfaceName);
        if ("stb".equalsIgnoreCase(uri.env) || "sit".equalsIgnoreCase(uri.env)) {
            originFilePath = FileResource.getDirpath(prePath,"", uri.appName, uri.interfaceName);
        }
        return filePath;
    }

    private JSONObject getOriginResponse(MockInterInfo mockInterInfo) throws MockException {
        if (mockInterInfo == null) {
            throw new MockException("文件转换为InterfaceInfo实例失败");
        }
        resDataType = mockInterInfo.getResType();
        return mockInterInfo.getResponse();
    }

    private MockInterInfo getInfoFromFile(String filePath) throws MockException {
        String interStr = null;
        interStr = FileResource.getResource(filePath);
        if (interStr == null&&originFilePath!=null) {
            interStr = FileResource.getResource(originFilePath);
            FileResource.copyFile(originFilePath,filePath);
        }
        return interStr == null ? null : JSONObject.parseObject(interStr, MockInterInfo.class);
    }


    public String getBodyData(HttpServletRequest request) {
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

       // str = DataConvert.getJsonStr(str);
        return data.toString();
    }

}
