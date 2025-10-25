package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.log.SmsLog;
import play.cache.NamedCache;
import play.libs.Json;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.*;

@Singleton
public class SmsUtils {

    public static final String SMS_VCODE_TEMPLATE = "本次验证码：**code**，10分钟内有效，请勿泄露。";
    public static final String SMS_NOTIFY_DEPOSIT_TEMPLATE = "您好%s，您于%s成功充值%s元，其中赠送%s元，当前余额为%s元，如有疑问，请联系客服";
    public static final String SMS_NOTIFY_CONSUME_TEMPLATE = "您好%s，您于%s消费%s元，当前余额为%s元，如有疑问，请联系客服";

    public static final int TYPE_VCODE = 1;
    public static final int TYPE_NOTIFY = 2;
    public static final int TYPE_BUSINESS = 3;

    @Inject
    CacheUtils cacheUtils;
    @Inject
    ConfigUtils configUtils;
    @Inject
    WSClient wsClient;
    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;

    public CompletionStage<ObjectNode> sendSMS(long orgId, String phoneNumber, String vcode, String content, int smsType) {
        return CompletableFuture.supplyAsync(() -> {
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            String appKey = configUtils.getShopConfigValue(PARAM_KEY_SMS_APP_KEY, orgId);
            String apiSecret = configUtils.getShopConfigValue(PARAM_KEY_SMS_API_SECRET, orgId);
            String templateId = configUtils.getPlatformConfigValue(PARAM_KEY_SMS_VCODE_TEMPLATE_ID);
            String placeHolder = "**VCODE_CONTENT**";
            if (smsType == TYPE_NOTIFY) {
                templateId = configUtils.getPlatformConfigValue(PARAM_KEY_SMS_NOTIFY_TEMPLATE_ID);
                placeHolder = "**NOTIFY_CONTENT**";
            } else if (smsType == TYPE_BUSINESS) {
                templateId = configUtils.getPlatformConfigValue(PARAM_KEY_SMS_BUSINESS_TEMPLATE_ID);
                placeHolder = "**PROMOTION_CONTENT**";
            }

            String requestUrl = configUtils.getPlatformConfigValue(PARAM_KEY_BATCH_SMS_REQUEST_URL);
            if (ValidationUtil.isEmpty(requestUrl)) {
                node.put("code", 500);
                node.put("reason", "短信请求地址为空");
                return node;
            }
            if (ValidationUtil.isEmpty(appKey)) {
                node.put("code", 500);
                node.put("reason", "appKey为空");
                return node;
            }
            if (ValidationUtil.isEmpty(apiSecret)) {
                node.put("code", 500);
                node.put("reason", "apiSecret为空");
                return node;
            }

            ArrayNode nodes = Json.newArray();
            ObjectNode contentNode = Json.newObject();
            contentNode.put("smsid", System.currentTimeMillis() + "");
            contentNode.put("mobile", phoneNumber);
            contentNode.put(placeHolder, content);
            nodes.add(contentNode);
            String smsSignId = configUtils.getShopConfigValue(PARAM_KEY_SMS_NOTIFY_SMSID, orgId);

            String param = "appkey=" + appKey + "&appsecret=" + apiSecret + "&smsSignId=" + smsSignId +
                    "&templateId=" + templateId +
                    "&content=" + Json.stringify(nodes);
            SmsLog smsLog = new SmsLog();
            smsLog.setPhoneNumber(phoneNumber);
            smsLog.setContent(requestUrl + "" + param);
            long unixStamp = System.currentTimeMillis();
            smsLog.setExtno(unixStamp + "");
            smsLog.setReqStatus("");
            smsLog.setRespStatus("");
            smsLog.setReqTime(unixStamp / 1000);
            smsLog.save();
            return wsClient.url(requestUrl).setContentType("application/x-www-form-urlencoded")
                    .post(param).thenApplyAsync((response) -> {
                        ObjectNode returnNode = Json.newObject();
                        ObjectNode result = (ObjectNode) Json.parse(response.getBody());
                        System.out.println("sms result:" + Json.stringify(result));
                        if (!ValidationUtil.isEmpty(vcode)) {
                            String key = cacheUtils.getSMSLastVerifyCodeKey(phoneNumber);
                            redis.set(key, vcode, 10 * 60);
                            if (null != result) {
                                String resultCode = result.findPath("code").asText();
                                if (resultCode.equalsIgnoreCase("0")) {
                                    returnNode.put("code", 200);
                                    //设置缓存，用于判断一分钟内请求短信多少
                                    String existRequestKey = EXIST_REQUEST_SMS + phoneNumber;
                                    redis.set(existRequestKey, existRequestKey, 60);
                                } else {
                                    System.out.println(response.getBody());
                                }
                                String msg = result.findPath("msg").asText();
                                smsLog.setMsgId(result.findPath("smsid").asText());
                                smsLog.setReqStatus(resultCode + "   " + msg);
                                if (result.has("data")) {
                                    ArrayNode data = (ArrayNode) result.findPath("data");
                                    if (null != data && data.size() > 0) {
                                        JsonNode nodeData = data.get(0);
                                        if (null != nodeData) {
                                            smsLog.setMsgId(nodeData.findPath("smsid").asText());
                                            String msg2 = result.findPath("msg").asText();
                                            String code2 = result.findPath("code").asText();
                                            smsLog.setRespStatus(code2 + "   " + msg2);
                                        }
                                    }
                                }
                            }
                        }
                        smsLog.setOrgId(orgId);
                        smsLog.setRespTime(System.currentTimeMillis() / 1000);
                        smsLog.save();
                        if (null != result) returnNode.set("result", result);
                        return returnNode;
                    }).toCompletableFuture().join();
        });
    }

}
