package com.nonobank.test.biz;

import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.Code;
import com.nonobank.test.entity.Config;
import com.nonobank.test.entity.MockInterInfo;
import com.nonobank.test.entity.ResponseResult;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by H.W. on 2018/4/22.
 */
@Component
public class ProcessConfig {

    private static Logger logger = LoggerFactory.getLogger(ProcessConfig.class);
    private Config config;
    public ProcessConfig(){}
    public ProcessConfig(Config config){
        this.config=config;
    }
    private  void processDelytime(int delayTime){
        if (delayTime > 0){
            try {
                Thread.sleep(delayTime * 1000);
                logger.info("查找到deleytime，响应"+delayTime+"秒后发出");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private  void  processHttpCode(HttpServletResponse response, String httpCode) throws MockException{
        if (StringUtils.isEmpty(httpCode)){
            return;
        }
        try {
            int status = Integer.valueOf(httpCode);
            response.setStatus(status);
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new MockException("httpCode 不合法："+httpCode);
        }

    }


    private   void processErrRate(HttpServletResponse response,String errRate) throws MockException {
        double d = Double.valueOf(errRate);
        if (!(d>=0&&d<=1)){
            throw new MockException("errRate应在0到1之间:"+errRate);
        }
        double temp = Math.random();
        if (d > temp){
            response.setStatus(404);
        }
    }

 public void process(Config config ,HttpServletResponse response) throws MockException{
     if (!StringUtils.isEmpty(config.getDelayTime())) {
       int  delayTime = Integer.valueOf(config.getDelayTime());
         processDelytime(delayTime);
     }
     if (!StringUtils.isEmpty(config.getHttpCode())) {

             processHttpCode(response, config.getHttpCode());

     }
     if (!StringUtils.isEmpty(config.getErrRate())) {
             processErrRate(response, config.getErrRate());

     }
 }
}
