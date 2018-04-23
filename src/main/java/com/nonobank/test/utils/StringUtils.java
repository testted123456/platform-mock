package com.nonobank.test.utils;

import com.nonobank.test.commons.Code;
import com.nonobank.test.commons.MockException;

/**
 * Created by H.W. on 2018/4/10.
 */
public class StringUtils extends org.springframework.util.StringUtils {


    public static String preJudge(String string) throws MockException {
        if (org.springframework.util.StringUtils.isEmpty(string)) {
            throw new MockException(Code.NOTNULL.getDes());
        }
        String str = string.trim();
        if (!str.startsWith("{") && !str.startsWith("<")) {
            throw new MockException(Code.NOT_JSON_OR_XML.getDes() + ",输入字串：" + string);
        }
        return str;
    }

    /**
     * 统计字符出现次数
     *
     * @param string
     * @param reg
     * @return
     */
    public static int getCount(String string, String reg) {
        int count = 0;
        for (int i = string.indexOf(reg); i < string.length(); i = string.indexOf(reg)) {
            if (i < 0) {
                break;
            }
            count++;
            string = string.substring(i + reg.length());
        }
        return count;
    }

    public static String[] split(String string, String split) {
        return split(string, split, 2);
    }

    private static String[] split(String str, String split, int i) {
        return str.split(split, i);
    }

}
