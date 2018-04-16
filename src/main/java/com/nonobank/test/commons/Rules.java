package com.nonobank.test.commons;

import com.nonobank.test.utils.RuleNode;
import com.nonobank.test.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by H.W. on 2018/4/10.
 */
public class Rules {
    private static final String RULE_PREX = "_";
    private static final List<String> list = new ArrayList<>();
    private static final Map<String, String> map = new HashMap<>();
    private static RuleNode matchNode;
    private static RuleNode ruleNode;


    static {
        map.put("(", ")");
        map.put("[", "]");
        matchNode = new RuleNode();
        ruleNode = new RuleNode();
    }

    /**
     * _AUTO
     * [_COUNT[_SUM[_INT(x.a.n)_INT(x.a.m)]]]
     */
    public static RuleNode parseRule(String string) {
        if (isStandardRule(string)){
            return ruleNode;
        }
        return null;
    }

    private static OperaEnum getOperaRule(String str) {
        return OperaEnum.getOperaEnum(str);
    }

    private static boolean isStandardRule(String str) {

        boolean flag = false;
        boolean splitFlag = false;
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(str)) {
            flag = true;
        }
        if (!str.startsWith("[") && !str.startsWith("(")) {
            return false;
        }
        char[] chars = str.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length ;i++) {
            char ch = chars[i];
            switch (ch) {
                case '[':
                    if (i != 0) {
                        splitFlag = true;
                    }
                    matchNode.push(ch);
                    break;
                case ']':
                    flag = isMatch(ch);
                    if (!flag){
                        return false;
                    }
                    splitFlag = true;
                    break;
                case '(':
                    splitFlag = true;
                    matchNode.push(ch);
                    break;
                case ')':
                    flag = isMatch(ch);
                    if (!flag){
                        return false;
                    }
                    splitFlag = true;
                    break;
                default:
                    if (splitFlag) {
                        if (!(sb.length() == 0)) {
                            ruleNode.push(sb.toString());
                            splitFlag = false;
                            sb = new StringBuilder();
                            sb.append(ch);
                        }
                    } else {
                        sb.append(ch);
                    }

            }
        }
        if (matchNode.isEmpty()){
            fillRuleNode(sb);
        }
        return flag;
    }

    private static void fillRuleNode(StringBuilder sb) {
        if (!(sb.length() == 0)) {
            ruleNode.push(sb.toString());
        }
    }

    private static boolean isMatch(char ch) {
        if (matchNode.isEmpty()) {
            return false;
        }
        Object obj = null;
        try {
            obj = matchNode.pop();
        } catch (MockException e) {
            e.printStackTrace();
            return false;
        }
        if ((map.get(String.valueOf(obj))).equals(String.valueOf(ch))) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) {
        isStandardRule("[_COUNT[_SUM[_INT(x.a.n)_INT(x.a.m)]]]");
     //   System.out.println("args = [" + args + "]");




/*        String json = "{\n" +
                "    \"certType\": \"IDENTITY_CARD\",\n" +
                "    \"honorType\": \"PENGYUAN_EDUCATION\",\n" +
                "    \"idCertNo\": \"441900199208247639\",\n" +
                "    \"isRequestNew\":false,\n" +
                "    \"params\": {\n" +
                "        \"documentNo\": \"441900199208247639\",\n" +
                "        \"name\": \"王家伟\"\n" +
                "    },\n" +
                "    \"systemKey\": \"90DD2C2CB3C94E4698ED5A54EE6CAD4F\"\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println("args = [" + args + "]");*/
    }
}
