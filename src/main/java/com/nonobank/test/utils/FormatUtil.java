package com.nonobank.test.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/12.
 */
public class FormatUtil extends j.m.XML {
    private final static String[] toolsresultItems = {
//                                "envValue",
//                                "name",
//                                "year",
//                                "periodValue",
//                                "count",
//                                "passcount",
//                                "fialcount",
            "beginTime",
            "endTime",
//                                "result"
    };

    private final static String xml_header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    private final static String TOOL_COUNT = "count";


    public static String ToolsResultFormat(JSONArray jsonArray) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        jsonArray.forEach(item -> {
            JSONObject jobj = (JSONObject) item;
            Map<String, Object> remap = new HashMap<String, Object>();
            for (String resultItem : toolsresultItems) {
                remap.put(resultItem, jobj.get(resultItem));
            }
            if ((int) jobj.get(TOOL_COUNT) != 1) {
                remap.put("passcount", jobj.get("passcount"));
                remap.put("fialcount", jobj.get("fialcount"));
            } else {
                remap.put("result", jobj.get("result"));
            }

            resultList.add(remap);
        });

        return FormatUtil.formatJson(JSONArray.toJSON(resultList).toString());
    }


    /**
     * 格式化
     *
     * @param jsonStr
     * @return
     * @author aaa
     * @Date 2015-10-14 下午1:17:35
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }


}
