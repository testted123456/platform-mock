package com.nonobank.test.api;

import com.nonobank.test.common.Assemble;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.MockException;
import com.nonobank.test.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by H.W. on 2018/4/25.
 */
@RestController
public class Mock {

    private static Logger logger = LoggerFactory.getLogger(Mock.class);


   /* @Autowired
    private Config config;*/
    @Autowired
    private Assemble assemble;


    @RequestMapping(value = "/**")
    public void assemResponse(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
/*
        if (!config.isEmpty()) {
            try {
                ProcessConfig.process(config, response);
            } catch (MockException e) {
                e.printStackTrace();
                Result result = Result.error(Code.Res.VALID_ERROR, e.getMessage());
                writer.write(result.toString());

                try {
                    writer = response.getWriter();

                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    writer.flush();
                }

            }
            return;
        }*/
        try {

            String res = assemble.getRes(request, response, "");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            writer.write(res);

        } catch (MockException | IOException e) {
            e.printStackTrace();
            Result result = Result.error(Code.ResultCode.VALIDATION_ERROR.getCode(), e.getMessage());
            writer.write(request.toString());
        } finally {
            writer.flush();
        }
    }


}
