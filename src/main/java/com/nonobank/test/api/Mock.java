package com.nonobank.test.api;

import com.nonobank.test.common.AcquireInfo;
import com.nonobank.test.DBResource.entity.MockException;
import com.nonobank.test.utils.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by H.W. on 2018/5/29.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class Mock {

    @Autowired
    private AcquireInfo acquireInfo;

    @RequestMapping("/**")
    public void getMockResponse(HttpServletRequest request, HttpServletResponse response) {
        String resStr = "";
        PrintWriter writer = null;
        try {
            resStr = acquireInfo.getResponse(request, response);
            writer = response.getWriter();
        } catch (IOException | MockException e) {
            e.printStackTrace();
        }
        writer.write(resStr);
        response.setCharacterEncoding("utf-8");
        if (StringUtils.isEmpty(response.getContentType())) {
            response.setContentType(request.getContentType());
        }
        writer.flush();

    }
}
