package utils.wechatpay;

/**
 * Created by Administrator on 2017/3/17.
 */
public class WechatConfig {

    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String WECHAT_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    public static final String WECHAT_SNS_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String NOTIFY_URL = "/v1/o/wechat/notify/";
    public static final String WECHAT_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static final String WECHAT_PAY_NOTIFY_URL = "/v1/o/wechat/pay_notify/";
    public static final String STABLE_ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";


    public static final String APP_ID = "";

    public static final String SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=ACCESS_TOKEN";

    public static final String WECHAT_MINI_APP_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
    public static final String WX_BAR_CODE_API_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    //0WWOCsdIDwpLaycLeZCu1m4GZrLPznsp

    // 到店自提通知  DONE
    public static final String TEMPLATE_MSG_ID_MERCHANT_ARRIVE = "0WWOCs_dIDwpLaycLeZCu1m4GZrLPznspgnzMB0soY0";

    //拼团失败通知　DONE
    public static final String TEMPLATE_MSG_ID_GROUPON_FAILED = "aI1HieBs_USjZmg82r7AmCAZIPeqXAc5PbPdhnmRQW8";

    //拼团成功通知 DONE
    public static final String TEMPLATE_MSG_ID_GROUPON_SUCCEED = "lIvq6O4mx-RsBPUpZF2iNKWkYQshsfsJqzHQ0b54Z-4";

    //拼团进度提醒
    public static final String TEMPLATE_MSG_ID_GROUPON_PROCESS = "L9MTyHjtebDjtqZ-vzYc_0n8fDrMr-af_s3H2sEe8xg";

    //订单发货通知
    public static final String TEMPLATE_MSG_ID_ORDER_SENT = "DoAJ8Di25IeNidaGJtYbemAXAiFnnSnlPXqyMOrF_1A";

    //付款成功通知
    public static final String TEMPLATE_MSG_ID_PAY_SUCCEED = "l5Fzqi77q2QR10GTroB1IDUaERS6iFYyLPnAv0tBXcw";

    //活动通知  DONE
    public static final String TEMPLATE_MSG_ID_ACTIVITY_NOTIFY = "2JArBszJcPCpElXITBlLTmVzKFizZS-i0c9TrpgfNWQ";
    //助力成功通知 DONE
    public static final String TEMPLATE_MSG_ID_SUCCEED_ASSIST = "Gc8tWqXDimM-5ghwHUYKG4nQ8bEDeQZ963cNiCbeGb8";
    //活动助力进度提醒 DONE
    public static final String TEMPLATE_MSG_ID_ASSIST_PROCESS_PROMPT = "bn2hchhnRmo-jFGEWdfx0YC5NatxrBI-GQCqpY9g38o";
    //签到提醒 DONE
    public static final String TEMPLATE_MSG_ID_SIGN_IN_NOTIFY = "ACqJwM9Ad8mTCvhRuK1QQ0cWXHg57HD5NDId2F6r2aM";

    //秒杀开始提醒
    public static final String TEMPLATE_MSG_ID_FLASH_SALE_NOTIFY = "_3Jg5oM8VRE0AjAENvmfsWBxFrmuQC-PBQ_VKX1xFNM";

    //砍价成功提醒
    public static final String TEMPLATE_MSG_ID_SUCCEED_BARGAIN = "tc5ic84RGdzN7Wq_xasxBiU0SBoxKyCvW-PEYI00gnw";
    //砍价失败通知
    public static final String TEMPLATE_MSG_ID_FAILED_BARGAIN = "zkYTS0vyFOxrPx_CrIAmQ9hbwdakUFR_RGH2jbXBQDk";
    //活动助力进度提醒 DONE
    public static final String TEMPLATE_MSG_ID_BARGAIN_PROCESS_PROMPT = "yhcWbLVfBm67TkLvsRFb_IKhiMQXahv8R3EV-Jypd3Q";
    public static final String WECHAT_TRANSFER = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

}
