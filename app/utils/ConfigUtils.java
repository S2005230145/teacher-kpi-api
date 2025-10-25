package utils;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import models.system.AdminConfig;
import models.system.ParamConfig;
import play.cache.NamedCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static constants.BusinessConstant.*;
import static constants.BusinessConstant.PARAM_KEY_CABINET_APP_ID;
import static constants.BusinessConstant.PARAM_KEY_CABINET_SECRET_CODE;
import static constants.BusinessConstant.PARAM_KEY_PLATFORM_FIX_FEE;

@Singleton
public class ConfigUtils {
    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;
    @Inject
    EncodeUtils encodeUtils;

    public String getShopConfigValue(String key, long orgId) {
        String value = "";
        String finalKey = key + ":" + orgId;
//        Optional<Object> accountOptional = redis.sync().get(finalKey);
//        if (accountOptional.isPresent()) {
//            value = (String) accountOptional.get();
//            if (!ValidationUtil.isEmpty(value)) return value;
//        }
        if (ValidationUtil.isEmpty(value)) {
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("key", key)
                    .eq("orgId", orgId)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null != config && !ValidationUtil.isEmpty(config.value)) {
                if (config.isEncrypt) {
                    value = encodeUtils.decrypt(config.value);
                } else value = config.value;
                redis.set(finalKey, value, 10);
            }
        }
        return value;
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


    public String getWechatMiniAppId() {
        return getPlatformConfigValue(PARAM_KEY_WECHAT_MINI_APP_ID);
    }

    public String getWechatMiniAppSecretCode() {
        return getPlatformConfigValue(PARAM_KEY_WECHAT_MINI_APP_SECRET_CODE);
    }

    public String getCabinetAppId() {
        return getPlatformConfigValue(PARAM_KEY_CABINET_APP_ID);
    }

    public String getCabinetSecretCode() {
        return getPlatformConfigValue(PARAM_KEY_CABINET_SECRET_CODE);
    }


    public String getWepaySpAppId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_APP_ID);
    }

    public String getWepaySpMchId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SP_MCH_ID);
    }

    public String getWepaySubMchId(long orgId) {
        return getShopConfigValue(PARAM_KEY_WE_PAY_SUB_MCH_ID, orgId);
    }

    public String getWepaySubMchId() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SUB_MCH_ID);
    }


    public String getWepaySubKeySerialNo() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SERIAL_NO);
    }

    public String getWepayAPIV3Key() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_API_V3_KEY);
    }

    public String getWepayPrivateKey() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_PRIVATE_KEY);
    }

    public String getCOSSecretId() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_ID);
    }

    public String getCOSSecretKey() {
        return getPlatformConfigValue(PARAM_KEY_COS_SECRET_KEY);
    }

    public long getPlatformFixFee() {
        String value = getPlatformConfigValue(PARAM_KEY_PLATFORM_FIX_FEE);
        if (!ValidationUtil.isEmpty(value)) return Long.parseLong(value);
        return 0;
    }

    public Config getWepayConfig() {
        // 初始化商户配置
        String spMchId = getWepaySpMchId();
        String merchantSerialNumber = getWepaySubKeySerialNo();
        String apiV3Key = getWepayAPIV3Key();
        String privateKey = getWepayPrivateKey();
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(spMchId)
                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                .privateKey(privateKey)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key)
                .build();
    }

    public Config getSubWepayConfig() {
        // 初始化商户配置
        String subMchId = getWepaySubMchId();
        String merchantSerialNumber = getMerchantKeySerialNo();
        String apiV3Key = getWepaySubAPIV3Key();
        String privateKey = getWepaySubPrivateKey();
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(subMchId)
                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                .privateKey(privateKey)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key)
                .build();
    }

    public String getMerchantKeySerialNo() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_KEY_SUB_SERIAL_NO);
    }

    public String getWepaySubAPIV3Key() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SUB_API_V3_KEY);
    }

    public String getWepaySubPrivateKey() {
        return getPlatformConfigValue(PARAM_KEY_WE_PAY_SUB_PRIVATE_KEY);
    }
}
