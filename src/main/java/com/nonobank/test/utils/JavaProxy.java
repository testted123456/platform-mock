package com.nonobank.test.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.nonobank.test.DBResource.entity.MockException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * Created by H.W. on 2018/4/27.
 */
public class JavaProxy {


    public final static String proxyCall(String url, String method, String str, String contentType) throws IOException, MockException {
        HttpUriRequest request = null;

        if ("GET".equalsIgnoreCase(method)) {
            request = createGetReq(url, str, contentType);
        } else if ("POST".equalsIgnoreCase(method)) {
            request = createPostReq(url, str, contentType);
        }
        return execut(request);
    }

    private static HttpPost createPostReq(String url, String str, String contentType) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(str));
        post.setHeader("content-type", contentType);
        return post;
    }

    private static HttpGet createGetReq(String url, String str, String contentType) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("content-type", contentType);
        return httpGet;
    }


    public static String execut(HttpUriRequest request) throws IOException, MockException {
        CloseableHttpResponse response = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            response = httpclient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                throw new MockException("转发异常 " + EntityUtils.toString(response.getEntity()));
            }
        } finally {
            response.close();
        }
    }


}