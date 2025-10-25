package controllers;

import constants.BusinessConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import play.Logger;
import utils.BizUtils;
import utils.MD5;
import utils.ValidationUtil;
import utils.alipay.AlipayCore;
import utils.wechatpay.WechatConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 微信支付辅助类
 */
@Singleton
public class WechatHelper {
    @Inject
    private BizUtils bizUtils;

    Logger.ALogger logger = Logger.of(WechatHelper.class);

    //微信统一下单参数设置
    public Map<String, String> generateWXParam(PayParam payParam, String ip, String tradeType) {
        Map<String, String> param = new TreeMap<>();
        String appId = bizUtils.getWechatMiniAppId();
        if (payParam.payMethod == BusinessConstant.PAYMENT_WEPAY_H5) appId = bizUtils.getWechatMpAppId();
        param.put("appid", appId);
        param.put("body", payParam.subject.trim());
//        param.put("device_info", "WEB");
        param.put("mch_id", bizUtils.getWechatMchId().trim());
        param.put("nonce_str", UUID.randomUUID().toString().replace("-", "").toUpperCase());
        param.put("notify_url", bizUtils.getDomain() + WechatConfig.WECHAT_PAY_NOTIFY_URL.trim());
        param.put("product_id", payParam.productionCode);
        param.put("sign_type", "MD5");//用默认的MD5时，签名里无需加入sign_type字段就可以验证通过，
//        改成HMAC-SHA256后需加入sign_type，并且要注意参数名称排序顺序，后面的pay_sign也一样
        param.put("out_trade_no", payParam.tradeNo.trim());
//        param.put("product_id", payParam.productionCode);
        param.put("spbill_create_ip", ip);
        param.put("total_fee", payParam.totalAmount + "");//微信以分为单位
        param.put("trade_type", tradeType);
        if (!ValidationUtil.isEmpty(payParam.openId)) param.put("openid", payParam.openId);
        String sign = signWithMd5(param);
        param.put("sign", sign);
        return param;
    }

    public String signWithMd5(Map<String, String> param) {
        Map<String, String> filterMap = AlipayCore.paraFilter(param, false);
        String strToSign = AlipayCore.createLinkString(filterMap);
        strToSign = strToSign + "&key=" + bizUtils.getWechatMchAppSecretCode();
        logger.info("strToSign:" + strToSign);
        String result = MD5.MD5Encode(strToSign).toUpperCase();
        return result;
    }

    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    //Map转xml数据
    public String convertMapToXML(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append("<![CDATA[").append(entry.getValue()).append("]]>");
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        try {
            return new String(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map<String, String> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String FEATURE = null;
        try {
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            factory.setFeature(FEATURE, true);

            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            FEATURE = "http://xml.org/sax/features/external-general-entities";
            factory.setFeature(FEATURE, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            factory.setFeature(FEATURE, false);

            // Disable external DTDs as well
            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            factory.setFeature(FEATURE, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
            // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
            // (http://cwe.mitre.org/data/definitions/918.html) and denial
            // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

            // remaining parser logic
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = getStringStream(xmlString);
            Document document = builder.parse(is);
            //获取到document里面的全部结点
            NodeList allNodes = document.getFirstChild().getChildNodes();
            Node node;
            Map<String, String> map = new HashMap<>();
            int i = 0;
            while (i < allNodes.getLength()) {
                node = allNodes.item(i);
                if (node instanceof Element) {
                    map.put(node.getNodeName(), node.getTextContent());
                }
                i++;
            }
            return map;
        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            Logger.info("ParserConfigurationException was thrown. The feature '" +
                    FEATURE + "' is probably not supported by your XML processor.");
            return new HashMap<>();
        }

    }

    public InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }
}
