package com.nonobank.test.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/25.
 */
public class DataStruct {
    private static final String SPLIT_POINT = ".";
    private static final String SPLIT_LIST_POINT = "@";

    public static void toOneDepth(Map<String, Object> originalMap, Map<String, Object> oneDepthMap, String keyPrex) {
        if (oneDepthMap == null || originalMap == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            String tempKey = keyPrex;
            if (obj instanceof Map) {
                if (StringUtils.isEmpty(tempKey)) {
                    tempKey = key.concat(SPLIT_POINT);
                } else {
                    tempKey = tempKey.concat(key).concat(SPLIT_POINT);
                }
                Map<String, Object> map = (Map<String, Object>) obj;
                toOneDepth(map, oneDepthMap, tempKey);
            } else if (obj instanceof List) {
                if (StringUtils.isEmpty(tempKey)) {
                    tempKey = key.concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                } else {
                    tempKey = tempKey.concat(key).concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                }
                List list = (List) obj;
                toOneDepth(list, oneDepthMap, tempKey);
            } else {
                if (StringUtils.isEmpty(tempKey)) {
                    oneDepthMap.put(key, obj);
                } else {
                    oneDepthMap.put(tempKey.concat(key), obj);
                }
            }
        }
    }

    private static void toOneDepth(List<Object> list, Map<String, Object> oneDepthMap, String keyPrex) {
        if (list == null) {
            return;
        }
        int length = list.size();
        for (int i = 0; i < length; i++) {
            String tempKey = keyPrex;
            Object obj = list.get(i);
            if (obj instanceof Map) {
                tempKey = tempKey.concat(String.valueOf(i)).concat(SPLIT_POINT);
                Map<String, Object> map = (Map<String, Object>) obj;
                toOneDepth(map, oneDepthMap, tempKey);
            } else if (obj instanceof List) {
                tempKey = tempKey.concat(String.valueOf(i)).concat(SPLIT_POINT).concat(SPLIT_LIST_POINT);
                List<Object> list1 = (List<Object>) obj;
                toOneDepth(list1, oneDepthMap, tempKey);
            } else {
                oneDepthMap.put(tempKey.concat(String.valueOf(i)), obj);
            }
        }
    }


}
