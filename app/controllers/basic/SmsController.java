package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import constants.RedisKeyConstant;
import controllers.BaseController;
import models.admin.ShopAdmin;
import models.user.Member;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import security.Secured;
import utils.SmsUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.RedisKeyConstant.EXIST_REQUEST_SMS;

/**
 * 用户控制类
 */
public class SmsController extends BaseController {

    Logger.ALogger logger = Logger.of(SmsController.class);
    @Inject
    SmsUtils smsUtils;

    /**
     * @api {POST} /v1/user/request_vcode/ 02请求短信验证码
     * @apiName requestVCode
     * @apiGroup SMS
     * @apiParam {string} phoneNumber 手机号码
     * @apiParam {string} [captcha] 校验码
     * @apiSuccess (Success 200){int} code 200 成功发送短信
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 发送失败
     * @apiSuccess (Error 40003) {int} code 40003 达到限制次数
     * @apiSuccess (Error 40004) {int} code 40004 校验码错误
     * @apiSuccess (Error 40005) {int} code 40005 请求验证码太频繁，请稍后再试
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> requestVCode(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode node = request.body().asJson();
            String phoneNumber = node.findPath("phoneNumber").asText();
            long orgId = businessUtils.getOrgIdFromHeader(request);
            if (!ValidationUtil.isPhoneNumber(phoneNumber)) return okCustomJson(CODE40001, "手机号码有误");
//            String captcha = node.findPath("captcha").asText();
//            if (ValidationUtil.isEmpty(captcha)) return okCustomJson(CODE40001, "请输入图形验证码");
//            String captchaKey = CAPTCHA_PREFIX + phoneNumber;
//            String correctCaptcha = cache.getOrElseUpdate(captchaKey, () -> "");
//            if (ValidationUtil.isEmpty(correctCaptcha)) return okCustomJson(CODE40001, "请输入图形验证码");
//            if (!correctCaptcha.equalsIgnoreCase(captcha)) return okCustomJson(CODE40001, "图形验证码有误");
            String ip = businessUtils.getRequestIP(request);
            if (!ValidationUtil.isValidIP(ip)) return okCustomJson(CODE500, "请求参数有误");
            String key = RedisKeyConstant.KEY_LOGIN_PREFIX + ip;
            Optional<Integer> optional = redis.sync().get(key);
            if (optional.isPresent()) {
                int count = optional.get();
                if (count > 20) return okCustomJson(CODE40001, "亲，服务器开小差了~");
                redis.set(key, count + 1, 1 * 60);
            } else {
                redis.set(key, 1, 1 * 60);
            }
            String existRequestKey = EXIST_REQUEST_SMS + phoneNumber;
            Optional<String> existOptional = redis.sync().get(existRequestKey);
            if (existOptional.isPresent()) {
                String exist = existOptional.get();
                if (!ValidationUtil.isEmpty(exist)) return okCustomJson(CODE40001, "一分钟内只能请求一次短信");
            }
            final String generatedVerificationCode = businessUtils.generateVerificationCode();
            String content = SmsUtils.SMS_VCODE_TEMPLATE.replace("**code**", generatedVerificationCode);
            return smsUtils.sendSMS(orgId, phoneNumber, generatedVerificationCode, content, SmsUtils.TYPE_VCODE)
                    .thenApplyAsync((result) -> ok(result)).toCompletableFuture().join();
        });

    }

    /**
     * @api {POST} /v1/user/request_user_vcode/ 03请求给用户号码发短信
     * @apiName requestUserVCode
     * @apiGroup SMS
     * @apiSuccess (Success 200){int} code 200 成功发送短信
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 发送失败
     * @apiSuccess (Error 40003) {int} code 40003 达到限制次数
     * @apiSuccess (Error 40004) {int} code 40004 校验码错误
     * @apiSuccess (Error 40005) {int} code 40005 请求验证码太频繁，请稍后再试
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> requestUserVCode(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            Member member = Member.find.byId(memberInCache.id);
            if (!ValidationUtil.isPhoneNumber(member.phoneNumber))
                return okCustomJson(CODE40001, "你的个人资料未填写手机号码");
            String existRequestKey = EXIST_REQUEST_SMS + member.phoneNumber;
            Optional<String> existOptional = redis.sync().get(existRequestKey);
            if (existOptional.isPresent()) {
                String exist = existOptional.get();
                if (!ValidationUtil.isEmpty(exist)) return okCustomJson(CODE40001, "一分钟内只能请求一次短信");
            }
            final String generatedVerificationCode = businessUtils.generateVerificationCode();
            String content = SmsUtils.SMS_VCODE_TEMPLATE.replace("**code**", generatedVerificationCode);
            return smsUtils.sendSMS(member.orgId, member.phoneNumber, generatedVerificationCode, content, SmsUtils.TYPE_VCODE)
                    .thenApplyAsync((result) -> ok(result)).toCompletableFuture().join();
        });
    }


}
