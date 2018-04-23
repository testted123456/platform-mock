package com.nonobank.test.biz;

import com.nonobank.test.api.Mock;
import com.nonobank.test.commons.MockException;
import com.nonobank.test.entity.URI;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理Path路径获取相关数据
 * Created by H.W. on 2018/4/13.
 */
public class ProcessPath {

    private static Logger logger = LoggerFactory.getLogger(ProcessPath.class);

    private static final String SLASH = "/";
    private static final String MOCK_SELF = "web-mock";

    /**
     * 分解请求URI,转换为接口的相关数据
     * @param str
     * @return
     */
    public static URI getURI(String str) throws MockException {
        logger.info("待处理mock接口请求路径为:" + str);
        URI uri = null;
        str = preprocessPath(str);
        if (canTrans(str)) {
            str = delSlash(str);
            if (containsEnv(str)) {
                String[] strings = str.split(SLASH, 3);
                uri = new URI(strings[0], strings[1], strings[2]);
            } else {
                String[] strings = str.split(SLASH, 2);
                uri = new URI("", strings[0], strings[1]);
            }
        }else {
            throw new MockException(str+"不能正常解析为MockName+InterfaceName");
        }
        return uri;
    }


    /**
     * 删除path路径的首尾斜线
     *
     * @param str
     * @return
     */
    private static String delSlash(String str) {
        if (str.startsWith(SLASH)) {
            str = str.substring(1);
        }
        if (str.endsWith(SLASH)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static boolean containsEnv(String str) {
        return str.startsWith("stb" + SLASH) || str.startsWith("sit" + SLASH);
    }

    /**
     * 判断path是否可分解为应用名+接口名
     *
     * @param str
     * @return
     */
    private static boolean canTrans(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith(SLASH) ? StringUtils.getCount(str, SLASH) > 1 : StringUtils.getCount(str, SLASH) > 0;
    }

    private static String preprocessPath(String path) throws MockException {
        path = delSlash(path);
        if (StringUtils.isEmpty(path)) {
            throw new MockException("接口路径为空");
        }

        if (path.startsWith(MOCK_SELF)) {
            path = path.replaceFirst(MOCK_SELF, "");
        }
        return path;
    }


}
