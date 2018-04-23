package com.nonobank.test.utils;

import com.nonobank.test.commons.Code;
import com.nonobank.test.commons.MockException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by H.W. on 2018/4/10.
 */
public class DataConvert {
    private static Logger logger = LoggerFactory.getLogger(DataConvert.class);

    public static JSONObject getJSONObject(String string) throws MockException {
        String str = StringUtils.preJudge(string);
        if (str.startsWith("{")) {
            return new JSONObject(str);
        }
        return XML.toJSONObject(str);
    }

    public static String getJsonStr(String string) throws MockException {
        String str = StringUtils.preJudge(string);
        if (str.startsWith("{")) {
            return str;
        } else if (str.startsWith("<")) {
            JSONObject jsonObject = XML.toJSONObject(str);
            return jsonObject.toString();
        }else {
            throw new MockException("字符串必须为XML或JSON");
        }

    }

    /**
     * XML 和 Json格式字符串互相转换
     *
     * @param string
     * @return
     */
    public static String xmlJsonLoopTrans(String string) throws MockException {
        String str = StringUtils.preJudge(string);
        if (str.startsWith("<")) {
            return XML.toJSONObject(str, false).toString();
        }
        JSONObject jsonObject = new JSONObject(string);
        return XML.toString(jsonObject, null);
    }

    public static JSONObject xml2JSON(String string) throws MockException {

        String str = StringUtils.preJudge(string);
        if (str.startsWith("<")) {
            return XML.toJSONObject(str, false);
        } else {
            throw new MockException(Code.NOT_XML.getDes() + ",输入字串：" + string);
        }

    }

    public static String json2Xml(String string) throws MockException {
        String str = StringUtils.preJudge(string);
        if (str.startsWith("{")) {
            return json2Xml(new JSONObject(str));
        } else {
            throw new MockException(Code.NOT_JSON.getDes() + ",输入字串：" + string);
        }
    }

    public static String json2Xml(JSONObject jsonObject) throws MockException {
        if (jsonObject == null) {
            throw new MockException(Code.NOTNULL.getDes());
        }
        return XML.toString(jsonObject, null);
    }


    /**
     * XML在最外层增加嵌套节点
     *
     * @param string  xml格式字串
     * @param tagName 节点名
     * @return
     */
    public static String xmlAppendNode(String string, String tagName) throws MockException {
        if (StringUtils.isEmpty(tagName) || StringUtils.isEmpty(string) || !string.trim().startsWith("<")) {
            logger.info("入参异常:" + " " + string + " tagName:" + tagName);
            throw new MockException(Code.ARGS_ILLEGAL.getDes());
        }
        StringBuilder sb = new StringBuilder();
        sb.append('<').append(tagName).append('>');
        sb.append(string);
        sb.append("</").append(tagName).append('>');
        return sb.toString();
    }


    public static void main(String[] args) throws MockException {
        String s = "<MasMessage xmlns=\"http://www.999bill.com/mas_cnp_merchant_interface\" xmlns=\"http://www.999bill.com/mas_cnp_merchant_interface1\">\n" +
                "  <version>auto</version>\n" +
                "  <QryTxnMsgContent>\n" +
                "    <txnType>auto</txnType>\n" +
                "    <txnStatus>S</txnStatus>\n" +
                "    <amount></amount>\n" +
                "    <merchantId>auto</merchantId>\n" +
                "    <terminalId>auto</terminalId>\n" +
                "    <entryTime>auto</entryTime>\n" +
                "    <transTime>auto</transTime>\n" +
                "    <externalRefNumber>auto</externalRefNumber>\n" +
                "    <voidFlag></voidFlag>\n" +
                "    <refNumber></refNumber>\n" +
                "    <responseCode>0003</responseCode>\n" +
                "    <storableCardNo>1234</storableCardNo>\n" +
                "  </QryTxnMsgContent>\n" +
                "  <TxnMsgContent>\n" +
                "    <txnType>PUR</txnType>\n" +
                "    <amount>1</amount>\n" +
                "    <merchantId>104110045112012</merchantId>\n" +
                "    <terminalId>00002012</terminalId>\n" +
                "    <entryTime>20170328101250</entryTime>\n" +
                "    <externalRefNumber>05000520170328101125233777088</externalRefNumber>\n" +
                "    <customerId>11443049</customerId>\n" +
                "    <transTime>20170328101253</transTime>\n" +
                "    <voidFlag>0</voidFlag>\n" +
                "    <refNumber>000014545747</refNumber>\n" +
                "    <responseCode>0003</responseCode>\n" +
                "    <responseTextMessage />\n" +
                "    <cardOrg>CU</cardOrg>\n" +
                "    <issuer>招商银行</issuer>\n" +
                "    <storableCardNo>6214859273</storableCardNo>\n" +
                "    <authorizationCode>173435</authorizationCode>\n" +
                "    <txnStatus>S</txnStatus>\n" +
                "  </TxnMsgContent>\n" +
                "</MasMessage>";


        JSONObject jsonObject3 = XML.toJSONObject(s);
        //Map map0 = jsonObject3.toMap();
        //  Map stransMap = Extract.transMap(map0);


        String xml = "<employees>\n" +
                "    <employee>\n" +
                "        <firstName>Bill</firstName>\n" +
                "        <lastName>Gates</lastName>\n" +
                "    </employee>\n" +
                "    <employee>\n" +
                "        <firstName>George</firstName>\n" +
                "        <lastName>Bush</lastName>\n" +
                "    </employee>\n" +
                "    <employee>\n" +
                "        <firstName>Thomas</firstName>\n" +
                "        <lastName>Thomas</lastName>\n" +
                "    </employee>\n" +
                "</employees>";
        JSONObject jsonObject = DataConvert.xml2JSON(xml);
        String json = jsonObject.toString();
        //Map<String, Object> map = jsonObject.toMap();
        // Map<String,Object> transMap = Extract.transMap(map);
        // String json  =jsonObject.toString();
        //   System.out.println(json);


        String sss = "<Res>\n" +
                "  <firstName>employees.employee.@0.firstName</firstName>\n" +
                "  <lastName>employees.employee.@0.lastName</lastName>\n" +
                "</Res>";

        //  Map map1 = Extract.structResMap(xml,sss);
        //System.out.println(XML.toString(new JSONObject(map1)));

        String jsonStr = "{\n" +
                "    \"employees\": [\n" +
                "        {\n" +
                "            \"firstName\": \"Bill\", \n" +
                "            \"lastName\": \"Gates\"\n" +
                "        }, \n" +
                "        {\n" +
                "            \"firstName\": \"George\", \n" +
                "            \"lastName\": \"Bush\"\n" +
                "        }, \n" +
                "        {\n" +
                "            \"firstName\": \"Thomas\", \n" +
                "            \"lastName\": \"Carter\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject jsonObject1 = new JSONObject(jsonStr);
      /*  Map map2 = jsonObject1.toMap();
        //Map<String,Object> transMap1 = Extract.transMap(map2);

        String xmlStr = DataConvert.json2Xml(jsonStr);
        String xmlStr1 = DataConvert.json2Xml(json);
        System.out.println(xmlStr);
        System.out.println(xmlStr1);*/
    }

}
