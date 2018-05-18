package com.nonobank.test.utils;

import com.nonobank.test.entity.Position;
import com.nonobank.test.entity.Type;

/**
 * Created by H.W. on 2018/4/24.
 */
public class StringUtils extends org.springframework.util.StringUtils {
    //获取字符串重复次数
    public static int getChildStrOccurNum(String originalStr, String childStr) {
        int count = 0;
        for (int i = originalStr.indexOf(childStr); i < originalStr.length(); i = originalStr.indexOf(childStr)) {
            if (i < 0) {
                break;
            }
            count++;
            originalStr = originalStr.substring(i + childStr.length());
        }
        return count;
    }

    //删除字符串中指定位置的子字符串
    public static String removeChildStr(String orginalStr, String childStr, Position pos) {
        String str = null;
        int length = childStr.length();
        switch (pos.getId()) {
            case 1:
                str = orginalStr.replaceAll(childStr, "");
                break;
            case 2:
                if (orginalStr.startsWith(childStr)) {
                    str = orginalStr.substring(length);
                }
                break;
            case 3:
                if (orginalStr.endsWith(childStr)) {
                    str = orginalStr.substring(0, orginalStr.length() - length);
                }
                break;
            case 4:
                if (orginalStr.startsWith(childStr)) {
                    orginalStr = orginalStr.substring(length);
                }
                if (orginalStr.endsWith(childStr)) {
                    orginalStr = orginalStr.substring(0, orginalStr.length() - length);
                }
                str =orginalStr;
                break;
        }

        return str;
    }

}
