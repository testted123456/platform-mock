package com.nonobank.test.common;

import com.nonobank.test.entity.Position;
import com.nonobank.test.entity.URI;
import com.nonobank.test.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by H.W. on 2018/4/25.
 */
public class ProcessPath {
    private static Logger logger = LoggerFactory.getLogger(ProcessPath.class);

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String MOCK_SELF = "web-mock/";

    public static URI getURL(String url) {
        url = StringUtils.removeChildStr(url, FILE_SEPARATOR, Position.INCLUSIVE);
        url = url.replaceFirst(MOCK_SELF , "");

        return _getURI(url);
    }

    private static URI _getURI(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        if (StringUtils.getChildStrOccurNum(url, FILE_SEPARATOR) <= 0) {
            return null;
        }
        if (url.startsWith("sit" + FILE_SEPARATOR) ||
                url.startsWith("stb" + FILE_SEPARATOR)) {
            if (StringUtils.getChildStrOccurNum(url, FILE_SEPARATOR) <= 1) {
                return null;
            }
            String[] strings = url.split(FILE_SEPARATOR, 3);
            URI uri = new URI(strings[0], strings[1], strings[2]);
            return uri;
        }
        String[] strings = url.split(FILE_SEPARATOR, 2);
        return new URI("", strings[0], strings[1]);
    }
}
