package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.BaseController;
import io.ebean.DB;
import io.ebean.Transaction;
import models.admin.ShopAdmin;
import models.campus.Campus;
import models.department.Department;
import models.org.Org;
import models.school.kpi.v3.Content;
import models.shop.Shop;
import models.user.Role;
import models.user.User;
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

import java.util.List;
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
     * @api {POST}  /v2/tk/login/noauth/ 01登录
     * @apiName login
     * @apiGroup New
     * @apiParam {string} phone 手机号.
     * @apiParam {string} password 密码, 6位至20位
     * @apiParam {Long} campusId 校区ID
     * @apiSuccess (Success 200){User} user信息
     * @apiSuccess (Success 200){long} code 200
     * @apiSuccess (Success 200){String} token 令牌
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40003) {int} code 40003 用户名或密码错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> login(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        String loginIP = businessUtils.getRequestIP(request);
        return CompletableFuture.supplyAsync(() -> {
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            String phone = jsonNode.findPath("phone").asText();
            String password = jsonNode.findPath("password").asText();
            long campusId = jsonNode.findPath("campusId").asLong();
            if (!ValidationUtil.isPhoneNumber(phone) || ValidationUtil.isEmpty(password))
                return okCustomJson(CODE40001, "手机号或密码的参数错误");
            if (campusId<=0) return okCustomJson(CODE40001, "缺少校区ID");

            User member = User.find.query().where()
                    .eq("phone", phone)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if(member!=null){
                member.setRole(Role.find.query().where().eq("id",member.getRoleId()).orderBy().asc("id").setMaxRows(1).findOne());
            }
            if (null == member) return okCustomJson(CODE40003, "手机号或密码错误");
            if (!member.password.equalsIgnoreCase(encodeUtils.getMd5WithSalt(password))) {
                if (businessUtils.uptoErrorLimit(request, KEY_LOGIN_MAX_ERROR_BY_ID + member.id, 10)) {
                    member.setStatus(ShopAdmin.STATUS_LOCK);
                    member.save();
                }
                return okCustomJson(CODE40003, "手机号或密码错误");
            }
            if (member.status == ShopAdmin.STATUS_LOCK)
                return okCustomJson(CODE40008, "帐号被锁定，请联系管理员");

            List<Department> departmentList = Department.find.query().where()
                    .eq("campus_id", campusId)
                    .findList();
            List<Long> departmentIds = departmentList.stream().map(Department::getId).toList();
            if(!departmentIds.contains(member.getDepartmentId())){
                return okCustomJson(CODE40003, "该用户的校区不正确");
            }

            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + loginIP);
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            //保存到缓存中
            String authToken = UUID.randomUUID().toString();
            result.put("token", authToken);
            handleCacheToken(member, authToken);
            businessUtils.deleteVcodeCache(phone);
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
    public void handleCacheToken(User shopAdmin, String authToken) {
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
     * @api {GET} /v1/tk/is_login/ 02是否已登录
     * @apiName isLogin
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 参数错误
     */
    public CompletionStage<Result> isLogin(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            User member = businessUtils.getUserIdByAuthToken2(request);
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            boolean isLogin = false;
            if (null != member) isLogin = true;
            result.put("login", isLogin);
            return ok(result);
        });
    }

    /**
     * @api {post} /v1/tk/user/new/ 03 注册
     * @apiName signUp
     * @apiGroup UserNew
     * @apiParam {string} userName 用户名
     * @apiParam {string} phone 手机号
     * @apiParam {String} password 登录密码 6-20位，不允许包含非法字符
     * @apiParam {String} typeName 职业
     * @apiParam {Long} roleId 角色ID
     * @apiParam {Long} departmentId 部门ID
     * @apiParam {int} status 状态
     * @apiSuccess (Success 200){int} code 200成功创建
     * @apiSuccess (Error 40003){int} code 40001 参数错误
     * @apiSuccess (Error 40001){int} code 40002 帐号已被注册
     * @apiSuccess (Error 40002){int} code 40003 无效的短信验证码
     * @apiSuccess (Error 40004){int} code 40004 登录密码无效
     * @apiSuccess (Error 40006){int} code 40006 无效的用户名
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> signUp(Http.Request request) {
        JsonNode json = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            if (null == json) return okCustomJson(40003, "参数错误");
            String userName = json.findPath("userName").asText();
            String password = json.findPath("password").asText();
            String phone = json.findPath("phone").asText();
            Long roleId=json.findPath("roleId").asLong();
            String typeName=(json.findPath("typeName") instanceof MissingNode ?null:json.findPath("typeName").asText());
            int status=json.findPath("status").asInt();
            long departmentId=json.findPath("departmentId").asLong();

            if(departmentId<0) return okCustomJson(CODE40001,"没有选择部门");

            if (ValidationUtil.isEmpty(userName))
                return okCustomJson(40006, "无效的用户名");
            if (!ValidationUtil.isValidPassword(password))
                return okCustomJson(CODE40001, "密码6-20位");
            if (!ValidationUtil.isPhoneNumber(phone))
                return okCustomJson(CODE40001, "手机号不正确");
            if (!ValidationUtil.isValidStatus(status))
                return okCustomJson(CODE40001, "状态异常");
            if (!ValidationUtil.isValidRoleId(roleId))
                return okCustomJson(CODE40001, "角色异常");

            User member = new User();
            member.setUserName(userName);
            member.setRoleId(roleId);
            member.setStatus(status);
            member.setPhone(phone);
            member.setDepartmentId(departmentId);
            if(typeName!=null){
                member.setTypeName(typeName);
            }
            User existMember = User.find.query().where()
                    .or()
                        .eq("user_name", userName)
                        .eq("phone",phone)
                    .endOr()
                    .setMaxRows(1)
                    .findOne();
            if (null != existMember) return okCustomJson(CODE40002, "您已注册过了");
//            long currentTime = dateUtils.getCurrentTimeByMills();
            try {
                DB.beginTransaction();
                member.setStatus(ShopAdmin.STATUS_NORMAL);
                member.setPassword(encodeUtils.getMd5WithSalt(password));
                member.save();
                DB.commitTransaction();
            } finally {
                DB.endTransaction();
            }
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v1/tk/admin_member/info/ 04 查看自己详情信息
     * @apiName getAdminMemberInfo
     * @apiGroup Admin-Authority
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {long} id 用户id
     * @apiSuccess {string} user_name 用户名
     * @apiSuccess {string} password 密码
     * @apiSuccess {string} type_name 类型名(班主任、年段长之类的)
     * @apiSuccess {int} status 状态
     * @apiSuccess {long} role_id 角色id
     * @apiSuccess {string} nick_name 角色类型名
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMemberInfo(Http.Request request) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            if (null == memberInCache) return unauth403();
            User member = User.find.byId(memberInCache.id);
            if (null == member) return unauth403();
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            result.put("id", member.id);
            result.put("user_name",member.userName);
            result.put("password", member.password);
            result.put("type_name", member.typeName);
            result.put("status", member.status);
            result.put("role_id", member.roleId);
            result.put("nick_name", member.role.nickName);
            return ok(result);
        });
    }


    /**
     * @api {POST} /v1/tk/reset_login_password/ 01 重置登录密码
     * @apiName resetLoginPassword
     * @apiGroup UserPassword
     * @apiParam {string} userName 帐号
     * @apiParam {string} newPassword 新密码
     * @apiSuccess (Success 200) {int} code 200
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
            String userName = node.findPath("userName").asText();
            userName = businessUtils.escapeHtml(userName);

            String newPassword = node.findPath("newPassword").asText();
            if (!checkPassword(newPassword)) return okCustomJson(CODE40003, "无效的密码");
            User member = User.find.query().where().eq("user_name", userName).setMaxRows(1).findOne();
            if (null == member) return okCustomJson(CODE40005, "该帐号不存在");
            member.setPassword(encodeUtils.getMd5WithSalt(newPassword));
            member.save();
            businessUtils.deleteVcodeCache(userName);
            return okJSON200();
        });
    }

    private boolean checkPassword(String password) {
        if (null == password || password.length() < 6 || password.length() > 20) return false;
        else return true;
    }

    /**
     * @api {POST} /v1/tk/set_login_password/ 02 设置/修改登录密码
     * @apiName setLoginPassword
     * @apiGroup UserPassword
     * @apiParam {string} oldPassword 旧密码
     * @apiParam {string} password 新密码
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
            long memberId = node.findPath("id").asLong();
            if (memberInCache.id != memberId) return okCustomJson(CODE403, "没有权限使用该功能");
            if (!checkPassword(newPassword)) return okCustomJson(CODE40003, "无效的密码");
            User member = User.find.byId(memberInCache.id);
            if (null == member) return okCustomJson(CODE40004, "该帐号不存在");
            if (!ValidationUtil.isEmpty(member.password)) {
                if (!member.password.equals(encodeUtils.getMd5WithSalt(oldPassword))) {
                    return okCustomJson(CODE40001, "原密码有误");
                }
            }
            member.setPassword(encodeUtils.getMd5WithSalt(newPassword));
            member.save();
            return okJSON200();
        });
    }

    /**
     * @api {POST} /v1/tk/user/is_admin/ 01 判断管理员
     * @apiName isAdmin
     * @apiGroup UserUpdate
     * @apiParamExample {json} 请求示例:
     * {}
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){boolean} status true
     *
     * @apiSuccess (Success 40005){int} code 40005
     * @apiSuccess (Success 40005){boolean} status false
     *
     * @apiSuccess (Success 40001){int} code 40001
     * @apiSuccess (Success 40001){String} reason 所有缺少信息
     */
    public CompletionStage<Result> isAdmin(Http.Request request){
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((memberInCache) -> {
            User user = User.find.byId(memberInCache.id);
            if(user==null) return okCustomJson(CODE40001,"未知用户");
            Role role = Role.find.byId(user.roleId);
            if(role==null) return okCustomJson(CODE40001,"未知权限");
            ObjectNode node = Json.newObject();
            if(role.nickName.contains("管理员")){
                node.put(CODE, CODE200);
                node.set("status",Json.toJson(true));
            }else{
                node.put(CODE, CODE40005);
                node.set("status",Json.toJson(false));
            }
            return ok(node);
        });
    }

    /**
     * @api {POST} /v1/tk/user/update/ 02 管理员更新用户(要先判断管理员)
     * @apiName updateUser
     * @apiGroup UserUpdate
     * @apiDescription 前端是否显示更新用户按钮
     * @apiParam {Long} id 用户ID
     * @apiParam {string} userName 用户名
     * @apiParam {string} phone 手机号
     * @apiParam {string} typeName 职业
     * @apiParam {int} status 状态
     * @apiParam {Long} roleId 角色ID
     * @apiParam {Long} departmentId 部门ID
     * @apiParamExample {json} 请求示例:
     * {
     *    "userName": "",//kpi标题名称
     *    "phone":"",
     *    "typeName":"",
     *    "status":"",
     *    "roleId":"",
     *    "departmentId":""
     * }
     * @apiSuccess (Success 200){int}code 200
     * @apiSuccess (Error 40001) {int}code 40001 无效的参数
     * @apiSuccess (Error 40004) {int}code 40004 该帐号不存在
     */
    public CompletionStage<Result> updateUser(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数出错");
            //条件
            long id = jsonNode.findPath("id").asLong();
            String userName = jsonNode.findPath("userName").asText();
            String phone = jsonNode.findPath("phone").asText();
            String typeName = jsonNode.findPath("typeName").asText();
            int status = jsonNode.findPath("status").asInt();
            long roleId = jsonNode.findPath("roleId").asLong();
            long departmentId = jsonNode.findPath("departmentId").asLong();

            if(id<=0) return okCustomJson(CODE40001,"没有id");

            User user = User.find.byId(id);
            if(user==null) return okCustomJson(CODE40001,"用户为空");

            if(departmentId>0) user.setDepartmentId(departmentId);
            if(roleId>0) user.setRoleId(roleId);
            user.setStatus(status);
            if(!ValidationUtil.isEmpty(typeName)) user.setTypeName(typeName);
            if(!ValidationUtil.isEmpty(phone)) user.setPhone(phone);
            if(!ValidationUtil.isEmpty(userName)) user.setUserName(userName);

            try(Transaction transaction = User.find.db().beginTransaction()){
                user.update();
                transaction.commit();
            }
            catch (Exception e) {
                return okCustomJson(CODE40002,"更新失败："+e);
            }

            return okCustomJson(CODE200,"更新成功");
        });
    }

    /**
     * @api {POST}  /v1/tk/login/ 07登录(后台)
     * @apiName loginBackEnd
     * @apiGroup Admin-Authority
     * @apiParam {jsonObject} data json串格式
     * @apiParam {string} phone 手机号
     * @apiParam {string} password 密码, 6位至20位
     * @apiSuccess (Success 200){String}  user_name 用户名
     * @apiSuccess (Success 200){long} id 用户id
     * @apiSuccess (Success 200){String} token token
     * @apiSuccess (Error 40001) {int} code 40001  参数错误
     * @apiSuccess (Error 40003) {int} code 40003 用户名或密码错误
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> loginBackEnd(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        String loginIP = businessUtils.getRequestIP(request);
        return CompletableFuture.supplyAsync(() -> {
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            String phone = jsonNode.findPath("phone").asText();
            String password = jsonNode.findPath("password").asText();
            if (ValidationUtil.isEmpty(phone) || ValidationUtil.isEmpty(password))
                return okCustomJson(CODE40001, "参数错误");

            User member = User.find.query().where()
                    .eq("phone", phone)
                    .orderBy().asc("id")
                    .setMaxRows(1).findOne();
            if(member!=null){
                member.setRole(Role.find.query().where().eq("id",member.getRoleId()).orderBy().asc("id").setMaxRows(1).findOne());
            }
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

            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + loginIP);
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            //保存到缓存中
            String authToken = UUID.randomUUID().toString();
            result.put("token", authToken);
            handleCacheToken(member, authToken);
            businessUtils.deleteVcodeCache(phone);
            redis.remove(KEY_LOGIN_MAX_ERROR_TIMES + member.id);
            return ok(result);
        });

    }

}
