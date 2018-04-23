package com.nonobank.test.utils;

import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by H.W. on 2018/4/22.
 */

@Component
public class PathUtil {
    @Value("${prePath}")
    private String prePath;
    private String separator = "/";


/*
    public String getAbsolutePath(String relativePath){
        return prePath+separator+relativePath+".json";
    }
    public String getOriginalPath(String relativePath){
        return getAbsolutePath(relativePath)+".json";
    }*/

    public  String getFilePath(String  relativePath){
        return prePath +separator+relativePath+".json";
    }

    public String getRelativePath(String url) throws MockException {
        URI uri = null;
        if (StringUtils.isEmpty(url)){
            throw  new MockException("url不能为空");
        }
        String temp = delSlash(url);
        if (canTrans(temp)){
            if (containsEnv(temp)) {
                String[] strings = temp.split(separator, 3);
                uri = new URI(strings[0], strings[1], strings[2]);
            }else {
                String[] strings = temp.split(separator, 2);
                uri = new URI("", strings[0], strings[1]);
            }
        }
        else {
            throw new MockException(url+"不能正常解析为MockName+InterfaceName");
        }
        return uri.toString();
    }


    /**
     * 删除path路径的首尾斜线
     * @param str
     * @return
     */
    private  String delSlash(String str) {
        if (str.startsWith(separator)) {
            str = str.substring(1);
        }
        if (str.endsWith(separator)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    /**
     * 判断path是否可分解为应用名+接口名
     *
     * @param str
     * @return
     */
    private  boolean canTrans(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith(separator) ? StringUtils.getCount(str, separator) > 1 : StringUtils.getCount(str, separator) > 0;
    }


    private   boolean containsEnv(String str) {
        return str.startsWith("stb" + separator) || str.startsWith("sit" + separator);
    }


}
