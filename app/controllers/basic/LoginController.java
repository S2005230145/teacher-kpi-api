package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.BaseController;
import io.ebean.DB;
import models.admin.ShopAdmin;
import models.org.Org;
import models.shop.Shop;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import utils.*;

import javax.inject.Inject;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

import static constants.RedisKeyConstant.KEY_LOGIN_MAX_ERROR_BY_ID;
import static constants.RedisKeyConstant.KEY_LOGIN_MAX_ERROR_TIMES;
import static utils.BizUtils.TOKEN_EXPIRE_TIME;

/**
 * 管理员登录控制器
 */
public class LoginController extends BaseController {
    Logger.ALogger logger = Logger.of(LoginController.class);
    @Inject
    EncodeUtils encodeUtils;


    /**
     * @api {POST} /v2/shop/login/noauth/ 01登录
     * @apiName login
     * @apiGroup Admin-Authority
     * @apiParam {jsonObject} data json串格式
     * @apiParam {string} username 用户名.
     * @apiParam {string} password 密码, 6位至20位
     * @apiParam {string} vcode 手机验证码
     * @apiSuccess (Success 200){String}  userName 用户名
     * @apiSuccess (Success 200){String}  realName 真名
     * @apiSuccess (Success 200){String}  lastLoginTimeForShow 最后登录时间
     * @apiSuccess (Success 200){String}  lastLoginIP 最后登录ip
     * @apiSuccess (Success 200){long} id 用户id
     * @apiSuccess (Success 200){String} token token
     * @apiSuccess (Success 200){String} groupName 所在组名
     * @apiSuccess (Error 40001) {int} code 40001  参数错误
     * @apiSuccess (Error 40003) {int} code 40003 用户名或密码错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> login(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        String loginIP = businessUtils.getRequestIP(request);
        return CompletableFuture.supplyAsync(() -> {
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            String userName = jsonNode.findPath("username").asText();
            String password = jsonNode.findPath("password").asText();
            String type = jsonNode.findPath("type").asText();
            String verificationCode = jsonNode.findPath("vcode").asText();
            if (ValidationUtil.isEmpty(userName) || ValidationUtil.isEmpty(password))
                return okCustomJson(CODE40001, "参数错误");
//            if (ValidationUtil.isEmpty(type) || !type.equalsIgnoreCase("type")) {
//                if (!businessUtils.checkVcode(verificationCode))
//                    return okCustomJson(CODE40002, "无效验证码");
//            }

            ShopAdmin member = ShopAdmin.find.query().where()
                    .eq("userName", userName)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if (null == member) return okCustomJson(CODE40003, "用户名或密码错误");
            if (!member.password.equalsIgnoreCase(encodeUtils.getMd5WithSalt(password))) {
                if (businessUtils.uptoErrorLimit(request, KEY_LOGIN_MAX_ERROR_BY_ID + member.id, 10)) {
                    member.setStatus(ShopAdmin.STATUS_LOCK);
                    member.save();
                }
                return okCustomJson(CODE40003, "用户名或密码错误");
            }
            if (member.status == ShopAdmin.STATUS_LOCK)
                return okCustomJson(CODE40008, "帐号被锁定，请联系管理员");
            if (!ValidationUtil.isEmpty(type)) {
                if (type.equalsIgnoreCase("pos")) {
                    if (member.shopId < 1) return okCustomJson(CODE40008, "请先绑定店铺");
                }
            }

            if (member.orgId < 1) return okCustomJson(CODE40008, "账号未关联店铺");
            Org org = Org.find.byId(member.orgId);
            if (null == org) return okCustomJson(CODE40008, "账号关联店铺不存在");
            if (org.status!=Org.STATUS_NORMAL) return okCustomJson(CODE40008, "店铺状态异常，禁止登录");

            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + loginIP);
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            //保存到缓存中
            String authToken = UUID.randomUUID().toString();
            result.put("token", authToken);
//            int deviceType = httpRequestDeviceUtils.getMobileDeviceType(request);
            handleCacheToken(member, authToken);
            businessUtils.deleteVcodeCache(userName);
            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + member.id);
            return ok(result);
        });

    }


    /**
     * 根据token保存cache
     *
     * @param shopAdmin
     * @param authToken
     */
    public void handleCacheToken(ShopAdmin shopAdmin, String authToken) {
        //authtoken -> platformtoken
        //uid:token:1 -> TOKEN_FOR_PLATFORM:UID
        //platformtoken -> Member

        //idToken => authToken
        // userToken(uid:token:1) => authToken
        //authToken => platformKey
        //platformKey => user
        //缓存新的token数据
        String tokenKey = cacheUtils.getMemberTokenKey(shopAdmin.id);
        Optional<String> optional = redis.sync().get(tokenKey);
        if (optional.isPresent()) {
            String oldAuthToken = optional.get();
            if (!ValidationUtil.isEmpty(oldAuthToken)) redis.remove(oldAuthToken);
        }
        //把旧的删除
        removeOldToken(tokenKey);
        int expireTime = getTokenExpireTime();
        redis.set(tokenKey, authToken, expireTime);
        shopAdmin.setPhoneNumber("");
        redis.set(authToken, shopAdmin, expireTime);
    }

    public int getTokenExpireTime() {
        return TOKEN_EXPIRE_TIME;
    }

    public void removeOldToken(String tokenKey) {
        Optional<Object> oldOptional = redis.sync().get(tokenKey);
        if (oldOptional.isPresent()) {
            String oldAuthToken = (String) oldOptional.get();
            if (!ValidationUtil.isEmpty(oldAuthToken)) {
                redis.remove(oldAuthToken);
            }
            redis.remove(tokenKey);
        }
    }

    public CompletionStage<Result> getActionHashCode(String action) {
        return CompletableFuture.supplyAsync(() -> {
            String hashCode = encodeUtils.getMd5WithSalt(action);
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.put("hashcode", hashCode);
            return ok(node);
        });
    }


    /**
     * @api {GET} /v2/shop/is_login/ 04是否已登录
     * @apiName isLogin
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess {boolean} login true已登录 false未登录
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     */
    public CompletionStage<Result> isLogin(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            ShopAdmin member = businessUtils.getUserIdByAuthToken2(request);
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            boolean isLogin = false;
            if (null != member) isLogin = true;
            result.put("login", isLogin);
            return ok(result);
        });
    }

    /**
     * @api {post} /v2/shop/user/new/ 04商户入驻注册
     * @apiName signUp
     * @apiGroup Admin-Authority
     * @apiParam {string} phoneNumber 手机号
     * @apiParam {string} vCode 短信验证码，预留
     * @apiParam {String} password 登录密码 6-20位，不允许包含非法字符
     * @apiSuccess (Success 200){int} code 200成功创建
     * @apiSuccess (Error 40003){int} code 40001 参数错误
     * @apiSuccess (Error 40001){int} code 40002 帐号已被注册
     * @apiSuccess (Error 40002){int} code 40003 无效的短信验证码
     * @apiSuccess (Error 40004){int} code 40004 登录密码无效
     * @apiSuccess (Error 40006){int} code 40006 无效的手机号码
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> signUp(Http.Request request) {
        JsonNode json = request.body().asJson();
        String ip = request.remoteAddress();
        return CompletableFuture.supplyAsync(() -> {
            if (null == json) return okCustomJson(40003, "参数错误");
            String phoneNumber = json.findPath("phoneNumber").asText();
            String password = json.findPath("password").asText();
            if (ValidationUtil.isEmpty(phoneNumber) || !ValidationUtil.isPhoneNumber(phoneNumber))
                return okCustomJson(40006, "无效的手机号码");
            if (!ValidationUtil.isValidPassword(password))
                return okCustomJson(CODE40001, "密码6-20位");

            //  String verificationCode = json.findPath("vCode").asText();
            //  if (ValidationUtil.isEmpty(verificationCode)) return okCustomJson(CODE40001, "短信验证码有误");
            // verificationCode = verificationCode.trim();
//            if (!businessUtils.checkVcode(phoneNumber, verificationCode)) return okCustomJson(CODE40001, "短信验证码有误");

            ShopAdmin member = new ShopAdmin();
            member.setPhoneNumber(phoneNumber);
            member.setRealName(phoneNumber);
            if (ValidationUtil.isEmpty(member.userName)) {
                member.setUserName(phoneNumber);
            }
            ShopAdmin existMember = ShopAdmin.find.query().where()
                    .eq("phoneNumber", phoneNumber)
                    .setMaxRows(1)
                    .findOne();
            if (null != existMember) return okCustomJson(CODE40002, "您已注册过了");
            long currentTime = dateUtils.getCurrentTimeByMills();
            try {
                DB.beginTransaction();
                member.setStatus(ShopAdmin.STATUS_NORMAL);
                member.setPassword(encodeUtils.getMd5WithSalt(password));
                member.setCreatedTime(currentTime);
                member.setLastLoginIP(ip);
                member.setLastLoginTime(currentTime);
                member.save();
                DB.commitTransaction();
            } finally {
                DB.endTransaction();
            }
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v2/shop/shop_on/ 06修改店铺资料
     * @apiName updateShop
     * @apiGroup Admin-Authority
     * @apiParam {long} id 店铺id
     * @apiParam {String} companyName 公司名称
     * @apiParam {String} licenseNumber 营业执照号
     * @apiParam {String} licenseImg 营业执照图片
     * @apiParam {String} lawName 法人
     * @apiParam {String} lawContactNumber 法人联系电话
     * @apiParam {String} idNo 法人身份证号
     * @apiParam {String} idCardFront 法人身份证正面
     * @apiParam {String} idCardBack 法人身份证反面
     * @apiParam {String} contactName 联系人
     * @apiParam {String} contactNumber 联系电话
     * @apiParam {String} contactAddress 联系地址
     * @apiParam {String} digest 公司简介
     * @apiParam {String} qualifications 资质
     * @apiParam {String} description 备注
     * @apiParam {String} approveNote 审核说明
     * @apiParam {String} applyCategories 入驻分类ID
     * @apiParam {String} applyCategoriesName 入驻分类名字
     * @apiParam {String} openBank 开户银行
     * @apiParam {String} openUserName 开户名字
     * @apiParam {String} openAccountName 开户帐号
     * @apiParam {String} openLicenseImg 开户许可证明
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateShop(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((member) -> {
            if (null == member || member.orgId < 1) return unauth403();
            long id = requestNode.findPath("id").asLong();
            if (id < 1) return okCustomJson(CODE40001, "参数错误");
            Shop param = Json.fromJson(requestNode, Shop.class);
            if (null == param) return okCustomJson(CODE40001, "参数错误");
            Shop shop = Shop.find.query().where()
                    .eq("creatorId", member.id)
                    .setMaxRows(1)
                    .findOne();
            long currentTime = dateUtils.getCurrentTimeByMills();
            if (null == shop) return okCustomJson(CODE40002, "该店铺不存在");
            if (requestNode.has("contactNumber")) shop.setContactNumber(param.contactNumber);
            if (requestNode.has("images")) shop.setImages(param.images);
            if (requestNode.has("rectLogo")) shop.setRectLogo(param.rectLogo);
            if (requestNode.has("avatar")) shop.setAvatar(param.avatar);
            if (requestNode.has("tags")) shop.setContactNumber(param.tags);
            if (requestNode.has("contactName")) shop.setContactName(param.contactName);
            if (requestNode.has("contactAddress")) shop.setContactAddress(param.contactAddress);
            if (requestNode.has("digest")) shop.setDigest(param.digest);
            if (requestNode.has("description")) shop.setDescription(param.description);
            shop.setUpdateTime(currentTime);
            shop.save();
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v2/shop/admin_member/info/ 08查看自己详情信息
     * @apiName getAdminMemberInfo
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} name 用户名
     * @apiSuccess {string} avatar avatar
     * @apiSuccess {int} groupId 所在分组id
     * @apiSuccess {long} shopId 店铺ID
     * @apiSuccess {String} avatar 头像
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMemberInfo(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            ShopAdmin member = ShopAdmin.find.byId(memberInCache.id);
            if (null == member) return unauth403();
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            result.put("id", member.id);
            result.put("name", member.realName);
            result.put("nickName", member.userName);
            result.put("avatar", member.avatar);
            result.put("status", member.status);
            result.put("shopName", member.shopName);
            result.put("shopId", member.shopId);
            result.put("introduction", "");
            int status = 0;
            Shop shop = Shop.find.query().where()
                    .eq("id", memberInCache.id)
                    .orderBy().asc("id")
                    .setMaxRows(1)
                    .findOne();
            if (null != shop) status = shop.status;
            result.put("shopStatus", status);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/shop/shop_info/ 08获取店铺资料
     * @apiName getAdminMemberInfo
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getShopInfo(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            ShopAdmin member = ShopAdmin.find.byId(memberInCache.id);
            if (null == member) return unauth403();
            Shop shop = Shop.find.byId(member.shopId);
            ObjectNode resultNode = Json.newObject();
            resultNode.put(CODE, CODE200);
            if (null != shop) {
                resultNode.set("shop", Json.toJson(shop));
            }
            return ok(resultNode);
        });
    }

    /**
     * @api {POST} /v2/shop/reset_login_password/ 10重置登录密码
     * @apiName resetLoginPassword
     * @apiGroup Admin-Authority
     * @apiParam {string} accountName 帐号
     * @apiParam {string} vcode 短信验证码
     * @apiParam {string} newPassword 新密码
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Error 40001) {int}code 40001无效的参数
     * @apiSuccess (Error 40002) {int}code 40002 无效的短信验证码
     * @apiSuccess (Error 40003) {int}code 40003 无效的密码
     * @apiSuccess (Error 40004) {int}code 40004无效的手机号码
     * @apiSuccess (Error 40005) {int}code 40005 该帐号不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> resetLoginPassword(Http.Request request) {
        JsonNode node = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            String accountName = node.findPath("accountName").asText();
            accountName = businessUtils.escapeHtml(accountName);
            String vcode = node.findPath("vcode").asText();
            if (!businessUtils.checkVcode(accountName, vcode))
                return okCustomJson(CODE40002, "无效手机号码/短信验证码");

            String newPassword = node.findPath("newPassword").asText();
            if (!checkPassword(newPassword)) return okCustomJson(CODE40003, "无效的密码");
            ShopAdmin member = ShopAdmin.find.query().where().eq("phoneNumber", accountName).setMaxRows(1).findOne();
            if (null == member) return okCustomJson(CODE40005, "该帐号不存在");
            member.setPassword(encodeUtils.getMd5WithSalt(newPassword));
            member.save();
            businessUtils.deleteVcodeCache(accountName);
            return okJSON200();
        });
    }

    private boolean checkPassword(String password) {
        if (null == password || password.length() < 6 || password.length() > 20) return false;
        else return true;
    }

    /**
     * @api {POST} /v2/shop/set_login_password/ 11设置/修改登录密码
     * @apiName setLoginPassword
     * @apiGroup User
     * @apiParam {string} [oldPassword] 旧密码
     * @apiParam {string} password 新密码
     * @apiParam {string} [vcode] 短信验证码
     * @apiParam {long} uid 用戶Id
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Error 40001) {int}code 40001 无效的参数
     * @apiSuccess (Error 40004) {int}code 40004 该帐号不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> setLoginPassword(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            JsonNode node = request.body().asJson();
            String newPassword = node.findPath("password").asText();
            String oldPassword = node.findPath("oldPassword").asText();
            String vcode = node.findPath("vcode").asText();
            long memberId = node.findPath("uid").asLong();
            if (memberInCache.id != memberId) return okCustomJson(CODE403, "没有权限使用该功能");
            if (!checkPassword(newPassword)) return okCustomJson(CODE40003, "无效的密码");
            ShopAdmin member = ShopAdmin.find.byId(memberInCache.id);
            if (null == member) return okCustomJson(CODE40004, "该帐号不存在");
            if (!businessUtils.checkVcode(member.phoneNumber, vcode))
                return okCustomJson(CODE40002, "短信验证码有误");
            if (!ValidationUtil.isEmpty(member.password)) {
                if (!member.password.equals(encodeUtils.getMd5WithSalt(oldPassword))) {
                    return okCustomJson(CODE40001, "原密码有误");
                }
            }
            member.setPassword(encodeUtils.getMd5WithSalt(newPassword));
            member.save();
            //TODO sms prompt password change
            return okJSON200();
        });
    }



}
