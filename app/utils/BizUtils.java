package utils;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.typesafe.config.Config;
import constants.BusinessConstant;
import io.ebean.DB;
import io.ebean.Expr;
import models.admin.AdminMember;
import models.admin.ShopAdmin;
import models.order.*;
import models.promotion.MemberCardCoupon;
import models.system.AdminConfig;
import models.system.PayMethod;
import models.user.Member;
import models.user.MemberBalance;
import models.user.MemberLevel;
import play.Logger;
import play.cache.NamedCache;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import utils.wechatpay.WechatConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.ADMIN_KEY_MEMBER_ID_AUTH_TOKEN_PREFIX;
import static constants.RedisKeyConstant.WECHAT_AUTHORIZER_ACCESS_TOKEN;
import static controllers.basic.UploaderManager.BUCKET;
import static controllers.basic.UploaderManager.IMG_URL_PREFIX;
import static models.order.Order.*;
import static models.promotion.MemberCoupon.STATUS_NOT_USE;

@Singleton
public class BizUtils {
    public static final int HOT_VIEW_LIST = 1;
    public static final int HOT_VIEW_DETAIL = 2;
    public static final int HOT_VIEW_SHOPPING_CART = 5;
    public static final int HOT_VIEW_ORDER = 100;
    Logger.ALogger logger = Logger.of(BizUtils.class);
    public static final int TOKEN_EXPIRE_TIME = 2592000;
    public static final int DEFAULT_MAIL_FEE = 1000;
    public static final int DEFAULT_LEVEL3_TOTAL_AWARD_COUNT = 9;
    public static final double SCORE_TO_ONE_TENTH = 0.02;//积分对分的比例，1积分=2分钱

    public static final int DEALER_TYPE_DIRECT = 1;
    public static final int DEALER_TYPE_INDIRECT = 2;
    public static final int DEALER_TYPE_SELF_TAKE_PLACE = 3;
    public static final int DEALER_TYPE_COUNT_AWARD = 4;
    @Inject
    CacheUtils cacheUtils;

    @Inject
    Config config;

    @Inject
    DateUtils dateUtils;

    @Inject
    EncodeUtils encodeUtils;

    @Inject
    WSClient wsClient;

    @Inject
    IPUtil ipUtil;
    @Inject
    BalanceUtils balanceUtils;

    @Inject
    @NamedCache("redis")
    protected play.cache.AsyncCacheApi redis;
    @Inject
    ConfigUtils configUtils;


    public static DecimalFormat DF = new DecimalFormat("0.0");

    public String getAuthTokenFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_TOKEN);
        if (authTokenHeaderValues.isPresent()) {
            return authTokenHeaderValues.get();
        }
        return "";
    }

    public ShopAdmin getUserIdByAuthToken2(Http.Request request) {
        String authToken = getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(authToken)) return null;
        Optional<ShopAdmin> optional = redis.sync().get(authToken);
        if (optional.isPresent()) {
            ShopAdmin member = optional.get();
            return member;
        }
        return null;
    }

    public CompletionStage<ShopAdmin> getUserIdByAuthToken(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> getUserIdByAuthToken2(request));
    }


    public String getUIDFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_TOKEN_UID);
        if (authTokenHeaderValues.isPresent()) {
            String authToken = authTokenHeaderValues.get();
            return authToken;
        }
        return "";
    }

    public String getAdminUidFromRequest(Http.Request request) {
        Optional<String> authTokenHeaderValues = request.getHeaders().get(KEY_AUTH_ADMIN_TOKEN);
        if (authTokenHeaderValues.isPresent()) {
            String authToken = authTokenHeaderValues.get();
            return authToken;
        }
        return "";
    }


    public long getCurrentTimeBySecond() {
        return System.currentTimeMillis() / 1000;
    }

    public int getTokenExpireTime() {
        int tokenExpireTime = config.getInt("token_expire_time");
        if (tokenExpireTime < 1) tokenExpireTime = TOKEN_EXPIRE_TIME;
        return tokenExpireTime;
    }

    public String getRequestIP(Http.Request request) {
        String ip = null;
        try {
            String remoteAddr = request.remoteAddress();
            String forwarded = request.getHeaders().get("X-Forwarded-For").get();
            String realIp = request.getHeaders().get(BusinessConstant.X_REAL_IP_HEADER).get();
            if (forwarded != null) {
                ip = forwarded.split(",")[0];
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = realIp;
            }
            if (ValidationUtil.isEmpty(ip)) {
                ip = remoteAddr;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ip == null ? "" : escapeHtml(ip);
    }

    public boolean checkVcode(String vcode) {
        Optional<Boolean> optional = redis.sync().get(vcode);
        if (optional.isPresent()) {
            boolean result = optional.get();
            return result;
        }
        return false;
    }

    public boolean checkVcode(String accountName, String vcode) {
        if (ValidationUtil.isPhoneNumber(accountName)) {
            String key = cacheUtils.getSMSLastVerifyCodeKey(accountName);
            Optional<String> optional = redis.sync().get(key);
            if (optional.isPresent()) {
                String correctVcode = optional.get();
                if (!ValidationUtil.isEmpty(correctVcode)) {
                    if (ValidationUtil.isVcodeCorrect(vcode) && ValidationUtil.isVcodeCorrect(correctVcode) && vcode.equals(correctVcode))
                        return true;
                }
            }
        } else return false;
        return false;
    }


    /**
     * 转义html脚本
     *
     * @param value
     * @return
     */
    public String escapeHtml(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("update", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("%", "\\%");
        value = value.replaceAll("union", "");
        value = value.replaceAll("load_file", "");
        value = value.replaceAll("outfile", "");
        return value;
    }

    public boolean setLock(String id, String operationType) {
        String key = operationType + ":" + id;
        try {
            Optional<String> optional = redis.sync().get(key);
            if (optional.isPresent()) return false;
            redis.sync().set(key, key, 5);
            return true;
        } catch (Exception e) {
            logger.error("getLock:" + e.getMessage());
            redis.remove(key);
        }
        return true;
    }

    /**
     * 解锁
     *
     * @param uid
     * @param operationType
     */
    public void unLock(String uid, String operationType) {
        redis.remove(operationType + ":" + uid);
    }


    public String getUserName(Member member) {
        String userName = "";
        if (null != member) {
            userName = member.realName;
            if (ValidationUtil.isEmpty(userName)) userName = member.nickName;
        }
        return userName;
    }


    public void push(ObjectNode node) {

    }

    public String limit20(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        if (value.length() > 20) return value.substring(0, 17) + "...";
        return value;
    }

    public String limit10(String value) {
        if (ValidationUtil.isEmpty(value)) return "";
        if (value.length() > 10) return value.substring(0, 7) + "...";
        return value;
    }

    public void updateOrderDetailStatus(Order order) {
        List<OrderDetail> detailList = OrderDetail.find.query().where().eq("orderId", order.id)
                .findList();
        if (detailList.size() > 0) {
            detailList.parallelStream().forEach((each) -> {
                each.setStatus(order.status);
            });
            DB.saveAll(detailList);
        }
    }


    public static BufferedImage desaturate(BufferedImage source) {
        ColorConvertOp colorConvert =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(source, source);
        return source;
    }


    public String convertScene(ObjectNode node) {
        String temp = Json.stringify(node);
        return temp.replaceAll("\\{", "(")
                .replaceAll("\\}", ")")
                .replaceAll("\"", "'");
    }


    public void setSkuStockCache(long skuId, long stock) {
//        String key = cacheUtils.getSkuStockCache(skuId);
//        asyncCacheApi.set(key, stock, 24 * 3600);
    }




    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137; // EARTH_RADIUS;
        return s;
    }

    public String getMemberName(ShopAdmin member) {
        String name = "";
        if (null != member) {
            name = member.realName;
            if (ValidationUtil.isEmpty(name)) name = member.realName;
        }
        return name;
    }

    public String getMemberName(Member member) {
        String name = "";
        if (null != member) {
            name = member.nickName;
            if (ValidationUtil.isEmpty(name)) name = member.realName;
        }
        return name;
    }

    public String getDomain() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_DEFAULT_HOME_PAGE_URL);
    }

    public String getWechatMpAppId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MP_APP_ID);
    }

    public String getWechatMpSecretCode() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MP_SECRET_CODE);
    }

    public String getWechatMiniAppId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MINI_APP_ID);
    }

    public String getWechatMiniAppSecretCode() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MINI_APP_SECRET_CODE);
    }

    public String getWechatMchId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MCH_ID);
    }

    public String getWechatMchAppSecretCode() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WECHAT_MCH_API_SECURITY_CODE);
    }



    public String generateVerificationCode() {
        Random ran = new Random();
        String code = String.valueOf(100000 + ran.nextInt(900000));
        return code;
    }

    public void deleteVcodeCache(String accountName) {
        String key = cacheUtils.getSMSLastVerifyCodeKey(accountName);
        if (!ValidationUtil.isEmpty(key)) redis.remove(key);
    }




    public String getWepaySpAppId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_APP_ID);
    }

    public String getWepaySpMchId() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_MCH_ID);
    }

    public String getWepaySubMchId(long orgId) {
        return configUtils.getShopConfigValue(PARAM_KEY_WE_PAY_SUB_MCH_ID, orgId);
    }

    public String getWepaySubKeySerialNo() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SERIAL_NO);
    }

    public String getWepayAPIV3Key() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WE_PAY_API_V3_KEY);
    }

    public String getWepayPrivateKey() {
        return configUtils.getPlatformConfigValue(PARAM_KEY_WE_PAY_PRIVATE_KEY);
    }

    //    public String getAlipayPublicKeyRSA2() {
//        return configUtils.getPlatformConfigValue(PARAM_KEY_ALIPAY_PUBLIC_KEY_RSA2);
//    }

    public void addOperationLog(Http.Request request, ShopAdmin admin, String note) {
        String ip = getRequestIP(request);
        String place = "";
        if (!ValidationUtil.isEmpty(ip)) place = ipUtil.getCityByIp(ip);
        request.getHeaders()
                .adding("adminId", admin.id + "")
                .adding("adminName", admin.realName)
                .adding("ip", ip)
                .adding("place", place)
                .adding("note", note);
    }

    public void addOperationLog(Http.Request request, AdminMember admin, String note) {
        String ip = getRequestIP(request);
        String place = "";
        if (!ValidationUtil.isEmpty(ip)) place = ipUtil.getCityByIp(ip);
        request.getHeaders()
                .adding("adminId", admin.id + "")
                .adding("adminName", admin.realName)
                .adding("ip", ip)
                .adding("place", place)
                .adding("note", note);
    }

    public boolean uptoErrorLimit(Http.Request request, String key, int max) {
        Optional<Integer> accessCountOptional = redis.sync().get(key);
        int accessCount = 0;
        if (accessCountOptional.isPresent()) {
            accessCount = accessCountOptional.get();
        }
        if (accessCount <= 0) {
            redis.set(key, 1, BusinessConstant.KEY_EXPIRE_TIME_2M);
        } else {
            int accessCountInt = accessCount + 1;
            if (accessCountInt > max) return true;
            redis.set(key, accessCountInt, BusinessConstant.KEY_EXPIRE_TIME_2M);
        }
        return false;
    }

    public String getCOSSecretId() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_ID);
    }

    public String getCOSSecretKey() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_KEY);
    }


    public String getPlatformConfigValue(String key) {
        String value = "";
        Optional<Object> accountOptional = redis.sync().get(key);
        if (accountOptional.isPresent()) {
            value = (String) accountOptional.get();
            if (!ValidationUtil.isEmpty(value)) return value;
        }
        if (ValidationUtil.isEmpty(value)) {
            AdminConfig config = AdminConfig.find.query().where()
                    .eq("key", key)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != config && !ValidationUtil.isEmpty(config.value)) {
                if (config.isEncrypt) {
                    value = encodeUtils.decrypt(config.value);
                } else value = config.value;
                redis.set(key, value);
            }
        }
        return value;
    }


    public String geneBarCode(String fileKey, ObjectNode paramNode, String page) {
        String accessTokenKey = WECHAT_AUTHORIZER_ACCESS_TOKEN;
        Optional<String> optional = redis.sync().get(accessTokenKey);
        String accessToken = "";
        if (optional.isPresent()) {
            accessToken = optional.get();
        }
        if (ValidationUtil.isEmpty(accessToken)) {
            System.out.println("accessToken为空");
            return "";
        }
        String url = WechatConfig.WX_BAR_CODE_API_URL + accessToken;
        ObjectNode param = Json.newObject();
        param.put("scene", convertScene(paramNode));
        param.put("page", page);
        param.put("width", 600);

        param.put("auto_color", false);
        ObjectNode colorParam = Json.newObject();
        colorParam.put("r", 0);
        colorParam.put("g", 0);
        colorParam.put("b", 0);
        param.set("line_color", colorParam);

        try {
            WSResponse response = wsClient.url(url).post(param).toCompletableFuture().get(20, TimeUnit.SECONDS);
            if (null != response && response.getBody().length() > 1000) {
                ByteString fileStream = response.getBodyAsBytes();
                byte[] bytes = fileStream.toArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                String barcodeImgUrl = uploadToOss(byteArrayInputStream, fileKey, bytes.length);
                return barcodeImgUrl;
            } else {
                logger.info("geneOrderBarCode:" + response.getBody());
            }
        } catch (Exception e) {
            logger.error("geneOrderBarCode:" + e.getMessage());
        }
        return "";
    }

    public String uploadToOss(ByteArrayInputStream buffer, String key, long length) {
        String accessId = getCOSSecretId();
        String secretKey = getCOSSecretKey();
        COSCredentials cred = new BasicCOSCredentials(accessId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(length);
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        objectMetadata.setContentType("image/jpg");
        com.qcloud.cos.model.PutObjectRequest putObjectRequest =
                new com.qcloud.cos.model.PutObjectRequest(BUCKET, key, buffer, objectMetadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        try {
            cosclient.putObject(putObjectRequest);
            return IMG_URL_PREFIX + key;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            cosclient.shutdown();
        }
        return "";
    }


    public String getAlipayServiceProviderId() {
        return getPlatformConfigValue(PARAM_KEY_ALI_SYS_SERVICE_PROVIDER_ID);
    }

    public String getAlipayPrivateKey() {
        return getPlatformConfigValue(PARAM_KEY_ALI_PAY_PRIVATE_KEY);
    }

    public String getAlipayPublicKey() {
        return getPlatformConfigValue(PARAM_KEY_ALI_PAY_PUBLIC_KEY);
    }

    public String getAlipayAgent(long orgId) {
        return configUtils.getShopConfigValue(PARAM_KEY_ALI_PAY_AGENT, orgId);
    }

    public String getWepayAPIV2Key() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_API_V2_KEY);
    }


    public int getDiscount(Member member) {
        int discount = 0;
        if (null != member) {
            if (member.birthday > 0) {
                if (dateUtils.isUnixStampToday(member.birthday)) {
                    String birthdayDiscount = configUtils.getShopConfigValue(PARAM_KEY_MEMBER_BIRTHDAY_DISCOUNT, member.orgId);
                    if (!ValidationUtil.isEmpty(birthdayDiscount)) {
                        int birthdayInt = Integer.parseInt(birthdayDiscount);
                        discount = birthdayInt;
                    }
                }
            }
        }
        String memberDay = configUtils.getShopConfigValue(PARAM_KEY_MEMBER_DAY, member.orgId);
        String memberDiscount = configUtils.getShopConfigValue(PARAM_KEY_MEMBER_DISCOUNT, member.orgId);
        if (!ValidationUtil.isEmpty(memberDay) && !ValidationUtil.isEmpty(memberDiscount)) {
            int memberDayInt = Integer.parseInt(memberDay);
            int memberDiscountInt = Integer.parseInt(memberDiscount);
            if (memberDayInt > 0 && memberDiscountInt > 0) {
                LocalDateTime now = LocalDateTime.now();
                int todayValue = now.getDayOfWeek().getValue();
                if (todayValue == memberDayInt) {
                    if (discount == 0) discount = memberDiscountInt;
                    else if (memberDiscountInt < discount) {
                        discount = memberDiscountInt;
                    }
                }
            }
        }
        if (member.level > 0) {
            MemberLevel memberLevel = MemberLevel.find.query().where()
                    .eq("level", member.level)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != memberLevel) {
                if (discount == 0) discount = memberLevel.orderDiscount;
                else if (memberLevel.orderDiscount < discount) {
                    discount = memberLevel.orderDiscount;
                }
            }
        }
        return discount;
    }



    public PayMethod getPayMethodName(int payMethodValue, long orgId) {
        PayMethod payMethod = PayMethod.find.query().where()
                .or(Expr.eq("orgId", 0), Expr.eq("orgId", orgId))
                .eq("attrValue", payMethodValue)
                .setMaxRows(1)
                .findOne();
        return payMethod;
    }



    public Order saveOrder(OrderParam param, Member member, int orderType, int printStatus) {
        Order order = new Order();
        if (null != member) {
            order.setUid(member.id);
            String name = member.realName;
            if (ValidationUtil.isEmpty(name)) name = member.nickName;
            if (ValidationUtil.isEmpty(name)) name = member.phoneNumber;
            order.setUserName(limit20(name));
//            order.setMemberNo(member.memberNo);
            order.setPhoneNumber(member.phoneNumber);
//            order.setMemberLevel(member.level);
        }
        order.setOrderType(orderType);
        order.setOrderNo(dateUtils.getCurrentTimeWithHMS() + IdGenerator.getId());
        order.setStatus(ORDER_STATUS_UNPAY);
        order.setOrgId(param.orgId);
        order.setTotalMoney(param.totalMoney);
        order.setRealPay(param.realPayMoney);
        order.setPaidUp(param.paidUp);
        order.setPostServiceStatus(0);
        order.setProductCount(param.productAmount);
        long currentTime = dateUtils.getCurrentTimeByMills();
        order.setUpdateTime(currentTime);
        order.setCreateTime(currentTime);
        order.setFilter(order.orderNo + " " + order.userName + " " + order.phoneNumber);
        return order;
    }


    /**
     * 下订单送积分
     */
    public long giveCreditScoreForOrder(Order order) {
        Member member = Member.find.byId(order.uid);
        long score = order.realPay / 100;
        if (null != member) {
            long currentTime = dateUtils.getCurrentTimeByMills();
//            if (isVipMember(member, currentTime)) {
//                score = score * 2;
//            }
            if (score > 0) {
                BalanceParam param = new BalanceParam.Builder()
                        .changeAmount(score)
                        .itemId(BusinessItem.SCORE)
                        .leftBalance(score)
                        .totalBalance(score)
                        .memberId(order.uid)
                        .desc("下单赠送积分:" + score)
                        .bizType(TRANSACTION_TYPE_GIVE_SCORE_FOR_ORDER).build();
                balanceUtils.saveChangeBalance(param, true);
            }
        }
        return score;
    }

    public String getDefaultAvatar(long orgId) {
        return configUtils.getShopConfigValue(PARAM_KEY_DEFAULT_AVATAR, orgId);
    }


    public long getRealPay(Order order) {
        long left = order.realPay - order.totalReturnMoney;
        if (left < 0) left = 0;
        return left;
    }

    public long getRealPaidup(Order order) {
        long left = order.paidUp - order.totalReturnMoney;
        if (left < 0) left = 0;
        return left;
    }

    public long getPaidup(Order order) {
        long paidUp = order.paidUp;
        MemberBalance balance = MemberBalance.find.query().where()
                .eq("uid", order.uid)
                .eq("itemId", BusinessItem.CASH)
                .setMaxRows(1).findOne();
        if (null != balance) {
            long sum = balance.realPay + balance.give;
            if (sum > 0) {
                paidUp = (long) ((balance.realPay * 1.00 / sum) * (order.realPay - order.totalReturnMoney));
            }
        }
        return paidUp;
    }


    public long calcGiveAmount(long charge) {
        long fiveMulti = charge / 50000;
        long twoMulti = (charge - fiveMulti * 50000) / 20000;
        long give = fiveMulti * 10000 + twoMulti * 2000;
        return give;
    }

    public String getWepaySubKeySerialNo(long orgId) {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SERIAL_NO);
    }


    public boolean upToIPLimit(Http.Request request, String key, int max) {
        String ip = getRequestIP(request);
        if (!ValidationUtil.isEmpty(ip)) {
            String accessCount = redis.sync().getOrElseUpdate(key + ip, () -> "");
            if (ValidationUtil.isEmpty(accessCount)) {
                redis.set(key + ip, "1", BusinessConstant.KEY_EXPIRE_TIME_2M);
            } else {
                int accessCountInt = Integer.parseInt(accessCount) + 1;
                if (accessCountInt > max) return true;
                redis.set(key + ip, String.valueOf(accessCountInt), BusinessConstant.KEY_EXPIRE_TIME_2M);
            }
        }
        return false;
    }


    public void giveBirthdayMemberCardCoupon(Member member) {
        models.user.MemberCardCoupon memberCardCoupon = new models.user.MemberCardCoupon();
        memberCardCoupon.setUid(member.id);
        memberCardCoupon.setNoLimitCount(false);
        String userName = member.realName;
        if (ValidationUtil.isEmpty(userName)) userName = member.nickName;
        memberCardCoupon.setUserName(userName);
        memberCardCoupon.setCardCouponId(0);
        memberCardCoupon.setShopId(0);
        memberCardCoupon.setTitle("生日赠送的八折券");
        memberCardCoupon.setContent("生日赠送的八折券");
        memberCardCoupon.setDigest("生日赠送的八折券");
        memberCardCoupon.setStatus(MemberCardCoupon.STATUS_NOT_USE);
        long currentTime = System.currentTimeMillis() / 1000;
        memberCardCoupon.setUpdateTime(currentTime);
        LocalDateTime now = LocalDateTime.now();
        int month = member.getBirthdayMonth();
        int day = member.getBirthdayDay();
        if (month < 1) month = now.getMonthValue();
        if (day < 1) day = now.getDayOfMonth();
        LocalDateTime birthday = LocalDateTime.of(now.getYear(), month, day, 0, 0, 0);
        long beginTime = Timestamp.valueOf(birthday).getTime() / 1000;
        memberCardCoupon.setBeginTime(beginTime);
        memberCardCoupon.setEndTime(beginTime + 24 * 3600);
        String txId = CARD_COUPON_PREFIX + dateUtils.getCurrentTimeWithHMS() + IdGenerator.getId();
        memberCardCoupon.setTransactionId("");
        memberCardCoupon.setSubId(txId);
        memberCardCoupon.setRealPay(0);
        memberCardCoupon.setPayType(0);
        memberCardCoupon.setCode(CARD_COUPON_FOR_GIVE);
        memberCardCoupon.setOnlyPickUp(false);
        memberCardCoupon.setDiscount(80);
        memberCardCoupon.save();
    }


    public long getOrgIdFromHeader(Http.Request request) {
        Optional<String> orgIdOptional = request.getHeaders().get("orgId");
        if (orgIdOptional.isPresent()) {
            String orgIdStr = orgIdOptional.get();
            if (!ValidationUtil.isEmpty(orgIdStr)) {
                long orgId = Long.parseLong(orgIdStr);
                return orgId;
            }
        }
        return 0;
    }

    public String getChineseNumber(int number) {
        if (number <= 10) {
            return getNumber(number);
        } else {
            int mod = number % 10;
            int devide = number / 10;
            String prefix = devide > 1 ? getNumber(devide) : "";
            return prefix + "十" + getNumber(mod);
        }
    }

    private static String getNumber(int number) {
        switch (number) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            case 10:
                return "十";
        }
        return "";
    }


    public CompletionStage<AdminMember> getAdminFromRequest(Http.Request request) {
        return CompletableFuture.supplyAsync(() ->
                {
                    Optional<AdminMember> optional = getAdminByAuthToken(request);
                    return optional.orElse(null);
                }
        );
    }

    public Optional<AdminMember> getAdminByAuthToken(Http.Request request) {
        String uidToken = getAdminUidFromRequest(request);
        if (ValidationUtil.isEmpty(uidToken)) return Optional.empty();
        Optional<String> uidOptional = redis.sync().get(uidToken);
        if (!uidOptional.isPresent()) return Optional.empty();
        String uid = uidOptional.get();
        if (ValidationUtil.isEmpty(uid)) return Optional.empty();
        String key = ADMIN_KEY_MEMBER_ID_AUTH_TOKEN_PREFIX + uid;
        Optional<String> optional = redis.sync().get(key);
        if (!optional.isPresent()) return Optional.empty();
        String token = optional.get();
        if (ValidationUtil.isEmpty(token)) return Optional.empty();
        Optional<AdminMember> adminMemberOptional = redis.sync().get(token);
        if (!adminMemberOptional.isPresent()) return Optional.empty();
        AdminMember adminMember = adminMemberOptional.get();
        if (null == adminMember) return Optional.empty();
        return Optional.of(adminMember);
    }

}
