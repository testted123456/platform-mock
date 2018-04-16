package com.nonobank.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.test.commons.MockException;
import j.m.XML;
import j.m.XMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/10.
 */

@Component
public class Extract {
    private Map<String, Object> transMap;
    private static final String SPLIT_CHAR = ".";
    private static final String SPLIT_LIST_CHAR = "@";
    private static final String XML_HEADER="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    /**
     * 根据输入数据构造响应数据
     */
    public Map<String, Object> structResMap(String req, String res) {
        Map<String, Object> resMap = null;
        try {
            String reqStr = StringUtils.preJudge(req);
            String resStr = StringUtils.preJudge(res);
            Map<String, Object> reqMap = DataConvert.getJSONObject(reqStr).toMap();
            resMap = DataConvert.getJSONObject(resStr).toMap();
            transMap = transMap(reqMap);
        } catch (MockException e) {
            e.printStackTrace();
        }
        traversal(resMap);
        return resMap;
    }


    /**
     * 根据输入数据构造响应数据
     */
    public Map<String, Object> structResMap(Map<String, Object> req, Map<String, Object> res) {
        try {
            transMap = transMap(req);
        } catch (MockException e) {
            e.printStackTrace();
        }
        traversal(res);
        return res;
    }

    /**
     * 将嵌套的map转换为深度为1的map
     *
     * @param reqMap
     * @return
     * @throws MockException
     */
    public Map<String, Object> transMap(Map<String, Object> reqMap) throws MockException {
        if (reqMap == null) {
            throw new MockException("待转换的map为空");
        }
        Map<String, Object> target = new HashMap<String, Object>();
        traversal(reqMap, target, "");
        return target;
    }


    /**
     * 将Map
     *
     * @param map
     * @param targetMap
     * @param keyPrexStr
     */
    private void traversal(Map<String, Object> map, Map<String, Object> targetMap, String keyPrexStr) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            String keyPrex = keyPrexStr;
            if (obj instanceof Map) {
                if (StringUtils.isEmpty(keyPrex)) {
                    keyPrex = key.concat(SPLIT_CHAR);
                } else {
                    keyPrex = keyPrex.concat(key).concat(SPLIT_CHAR);
                }
                Map<String, Object> e = (Map<String, Object>) obj;
                traversal(e, targetMap, keyPrex);
            } else if (obj instanceof List) {
                if (StringUtils.isEmpty(keyPrex)) {
                    keyPrex = key.concat(SPLIT_CHAR).concat(SPLIT_LIST_CHAR);
                } else {
                    keyPrex = keyPrex.concat(key).concat(SPLIT_CHAR).concat(SPLIT_LIST_CHAR);
                }
                List list = (List) obj;
                traversal(list, targetMap, keyPrex);
            } else {
                if (StringUtils.isEmpty(keyPrex)) {
                    targetMap.put(key, obj);
                } else {
                    targetMap.put(keyPrex.concat(key), obj);
                }
            }
        }
    }

    private void traversal(List<Object> list, Map<String, Object> targetMap, String keyPrex) {
        if (list == null) {
            return;
        }
        int length = list.size() > 0 ? list.size() : 0;
        for (int i = 0; i < length; i++) {
            String listKey = keyPrex;
            Object obj = list.get(i);
            if (obj instanceof Map) {
                listKey = listKey.concat(String.valueOf(i)).concat(SPLIT_CHAR);
                Map<String, Object> map = (Map<String, Object>) obj;
                traversal(map, targetMap, listKey);
            } else {
                targetMap.put(listKey.concat(String.valueOf(i)), obj);
            }
        }
    }

    /**
     * 遍历response，构造响应数据
     *
     * @param map
     */
    private void traversal(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                traversal((Map<String, Object>) obj);
            } else if (obj instanceof List) {
                traversal((List) obj);
            } else {
                String tmpKey = (String) obj;
                Object value = transMap.get(tmpKey);
                if (value.getClass().isArray()) {//
                    String[] strings = (String[]) value;
                    String temp = null;
                    for (int i = 0; i < strings.length; i++) {
                        if (i == 0) {
                            temp = strings[i] ;
                        } else {
                            temp = temp+"," + strings[i];
                        }
                    }
                    value = temp;
                }
                entry.setValue(value);
            }
        }
    }

    /**
     * 遍历response，构造响应数据
     *
     * @param list
     */
    private void traversal(List<Object> list) {
        if (list == null) {
            return;
        }
        int length = list.size() > 0 ? list.size() : 0;
        for (int i = 0; i < length; i++) {
            Object obj = list.get(i);
            if (obj instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) obj;
                traversal(map);
            } else {
                if (transMap.containsKey((String) obj)) {
                    list.set(i, transMap.get((String) obj));
                }
            }
        }
    }

    public String getResponse(String req, String res, String type) {
        String json = JSONObject.toJSONString(structResMap(req, res));
        if ("xml".equalsIgnoreCase(type)) {
            XMap xMap = XMap.fromJSON(json);
            json = XML.toPrettyXML(xMap);
        }
        return json;
    }

    public String getResponse(Map<String, Object> req, Map<String, Object> res, String type) throws MockException {
        Map<String, Object> map = structResMap(req, res);
        String str = JSONObject.toJSONString(map);
        if ("xml".equalsIgnoreCase(type)) {
        /*   XMap xMap = XMap.fromJSON(str);
            str = XML.toPrettyXML(xMap);*/
            str = DataConvert.json2Xml(str);
            str = XML_HEADER+"\n"+str;
        }
        return str;
    }

   /* public static void main(String[] args) throws MockException {
        Map<String, Object> map3;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(map2);
        map1.put("map1key1", "map1value1");
        map1.put("map1key2", "map1value2");
        map2.put("map2key1", "map2value1");
        map2.put("map2key2", "map2value2");
        map1.put("map1map2", map2);
        map.put("mapkey1", "mapvalue1");
        map.put("mapkey2", "mapvalue2");
        map.put("mapmap1", map1);
        map.put("maplist", list);
        String xmlStr = XML.toString(new JSONObject(map),"XML");
        System.out.println(xmlStr);

        map3 = XML.toJSONObject(xmlStr).toMap();
        map3 = transMap(map3);

        System.out.println(map3.containsKey("map2key2"));


        String res = "<Res>\n" +
                "  <field1>XML.maplist.map2key2</field1>\n" +
                "</Res>";


        Map map4 = Extract.structResMap(xmlStr, res);
        System.out.println(XML.toString(new JSONObject(map4)));
    }*/
}
