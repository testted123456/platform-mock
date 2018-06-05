package com.nonobank.test.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */


public class Convert {

    private static final String SPLIT_POINT = ".";
    private static final String SPLIT_LIST_POINT = "@";

    public static void sourceMapToSimpleMap(Map<String, Object> sourceMap, Map<String, Object> simpleMap, String prexKey) {
        if (simpleMap == null || sourceMap == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            String tempKey = prexKey;
            if (obj instanceof Map) {
                if (StringUtils.isEmpty(tempKey)) {
                    tempKey = key.concat(SPLIT_POINT);
                } else {
                    tempKey = tempKey.concat(key).concat(SPLIT_POINT);
                }
                Map<String, Object> map = (Map<String, Object>) obj;
                sourceMapToSimpleMap(map, simpleMap, tempKey);
            } else if (obj instanceof List) {
                if (StringUtils.isEmpty(tempKey)) {
                    tempKey = key.concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                } else {
                    tempKey = tempKey.concat(key).concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                }
                List list = (List) obj;
                sourceMapToSimpleMap(list, simpleMap, tempKey);
            } else {
                if (StringUtils.isEmpty(tempKey)) {
                    simpleMap.put(key, obj);
                } else {
                    simpleMap.put(tempKey.concat(key), obj);
                }
            }
        }
    }

    private static void sourceMapToSimpleMap(List<Object> list, Map<String, Object> simpleMap, String prexKey) {
        if (list == null) {
            return;
        }
        int length = list.size();
        for (int i = 0; i < length; i++) {
            String tempKey = prexKey;
            Object obj = list.get(i);
            if (obj instanceof Map) {
                tempKey = tempKey.concat(String.valueOf(i)).concat(SPLIT_POINT);
                Map<String, Object> map = (Map<String, Object>) obj;
                sourceMapToSimpleMap(map, simpleMap, tempKey);
            } else if (obj instanceof List) {
                tempKey = tempKey.concat(String.valueOf(i)).concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                List<Object> templist = (List<Object>) obj;
                sourceMapToSimpleMap(templist, simpleMap, tempKey);
            } else {
                simpleMap.put(tempKey.concat(String.valueOf(i)), obj);
            }
        }
    }


}
