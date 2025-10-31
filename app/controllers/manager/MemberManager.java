package controllers.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseAdminSecurityController;
import io.ebean.DB;
import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.admin.AdminMember;
import models.admin.Group;
import models.admin.GroupUser;
import models.log.BalanceLog;
import models.order.Order;
import models.order.OrderDetail;
import models.org.Org;
import models.shop.Shop;
import models.user.Member;
import models.user.MemberBalance;
import myannotation.EscapeHtmlSerializer;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.EncodeUtils;
import utils.HttpRequestDeviceUtils;
import utils.Pinyin4j;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static constants.BusinessConstant.*;
import static constants.RedisKeyConstant.KEY_LOGIN_MAX_ERROR_TIMES;
import static utils.BusinessItem.CASH;

/**
 * 用户管理
 */
public class MemberManager extends BaseAdminSecurityController {
    Logger.ALogger logger = Logger.of(MemberManager.class);

    @Inject
    EscapeHtmlSerializer escapeHtmlSerializer;

    @Inject
    EncodeUtils encodeUtils;

    @Inject
    Pinyin4j pinyin4j;


    /**
     * @api {POST} /v1/tk/members/?page=&uid=&filter= 01获取用户列表
     * @apiName listMembers
     * @apiGroup ADMIN_MEMBER
     * @apiParam {long} uid uid
     * @apiParam {long} orgId orgId
     * @apiParam {long} shopId shopId
     * @apiParam {int} page page
     * @apiParam {String} filter realName/nickName/phoneNumber/dealerCode
     * @apiParam {int} status 0all 1normal 2pause
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 用户列表
     * @apiSuccess (Success 200){long} id 用户ID
     * @apiSuccess (Success 200){int} status 用户状态1正常2锁定
     * @apiSuccess (Success 200){string} realName 实名
     * @apiSuccess (Success 200){string} nickName 昵称
     * @apiSuccess (Success 200){string} phoneNumber 手机号
     * @apiSuccess (Success 200){string} description 备注
     * @apiSuccess (Success 200){string} agentCode 代理编号
     * @apiSuccess (Success 200){string} updateTime 更新时间
     * @apiSuccess (Success 200){string} createdTime 创建时间
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> listMembers(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            JsonNode requestNode = request.body().asJson();
            if (null == requestNode) return okCustomJson(CODE40001, "参数错误");
            int page = requestNode.findPath("page").asInt();
            int status = requestNode.findPath("status").asInt();
            int hasDealer = requestNode.findPath("hasDealer").asInt();
            long uid = requestNode.findPath("uid").asLong();
            long dealerId = requestNode.findPath("dealerId").asLong();
            long shopId = requestNode.findPath("shopId").asLong();
            long orgId = requestNode.findPath("orgId").asLong();

            String filter = requestNode.findPath("filter").asText();
            Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            ExpressionList<Member> expressionList = Member.find.query().where();
            if (uid > 0) expressionList.eq("id", uid);
//            if (status > 0) expressionList.eq("status", status);
            if (dealerId > 0) expressionList.eq("dealerId", dealerId);
            if (shopId > 0) expressionList.eq("shopId", shopId);
            if (orgId > 0) expressionList.eq("orgId", orgId);
            if (hasDealer > 0) {
                if (hasDealer == 1) expressionList.gt("dealerId", 0);
                else expressionList.eq("dealerId", 0);
            }
            if (requestNode.has("shopIdList")) {
                ArrayNode orgIdList = (ArrayNode) requestNode.findPath("shopIdList");
                Set<Long> orgIdSet = new HashSet<>();
                orgIdList.forEach((each) -> orgIdSet.add(each.asLong()));
                if (orgIdSet.size() > 0) expressionList.in("shopId", orgIdSet);
            }

            if (!ValidationUtil.isEmpty(filter)) {
                String orFilter = escapeHtmlSerializer.escapeHtml(filter);
                orFilter = "%" + orFilter + "%";
                expressionList.or(
                        Expr.or(Expr.ilike("realName", orFilter), Expr.like("contactPhoneNumber", orFilter)),
                        Expr.or(Expr.ilike("nickName", orFilter), Expr.ilike("orgName", orFilter))
                );
            }
            int members = expressionList.findCount();
            PagedList<Member> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .findPagedList();
            List<Member> list = pagedList.getList();
            int pages = pagedList.getTotalPageCount();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.put("pages", pages);
            node.put("totalMembers", members);
            node.set("list", Json.toJson(list));
            businessUtils.addOperationLog(request, admin, "执行查询用户列表");
            return ok(node);
        });
    }

    /**
     * @api {GET} /v1/tk/members/:memberId/ 02获取用户详情
     * @apiName getUser
     * @apiGroup ADMIN_MEMBER
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} pages 分页
     * @apiSuccess (Success 200){JsonArray} list 用户列表
     * @apiSuccess (Success 200){long} id 用户ID
     * @apiSuccess (Success 200){int} status 用户状态1正常2锁定
     * @apiSuccess (Success 200){string} realName 实名
     * @apiSuccess (Success 200){string} nickName 昵称
     * @apiSuccess (Success 200){string} phoneNumber 手机号
     * @apiSuccess (Success 200){string} description 备注
     * @apiSuccess (Success 200){long} birthday 生日
     * @apiSuccess (Success 200){String} idCardNo 身份证号
     * @apiSuccess (Success 200){String} licenseNo 营业执照
     * @apiSuccess (Success 200){String} licenseImgUrl 营业执照图片地址
     * @apiSuccess (Success 200){string} agentCode 代理编号
     * @apiSuccess (Success 200){string} idCardNo 身份证号码
     * @apiSuccess (Success 200){string} licenseNo 营业执照
     * @apiSuccess (Success 200){int} gender 0：未知、1：男、2：女
     * @apiSuccess (Success 200){String} city 城市
     * @apiSuccess (Success 200){String} province 省份
     * @apiSuccess (Success 200){String} country 国家
     * @apiSuccess (Success 200){String} shopName 店铺
     * @apiSuccess (Success 200){String} contactPhoneNumber 联系电话
     * @apiSuccess (Success 200){String} contactAddress 联系地址
     * @apiSuccess (Success 200){String} businessItems 经营类目
     * @apiSuccess (Success 200){String} images 图片，多张，以逗号隔开
     * @apiSuccess (Success 200){string} createdTime 创建时间
     */
    public CompletionStage<Result> getMember(Http.Request request, long uid) {
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            Member member = Member.find.byId(uid);
            if (null == member) return okCustomJson(CODE40001, "用户不存在");
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("totalOrderMoney", member.totalOrderMoney);
            List<MemberBalance> list = MemberBalance.find.query().where().eq("uid", uid).findList();
            result.put(CODE, CODE200);
            result.set("balanceList", Json.toJson(list));
            businessUtils.addOperationLog(request, admin, "查看用户详情，uid:" + member.id);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v1/tk/members/status/ 03锁定/解锁用户
     * @apiName setMemberStatus
     * @apiGroup ADMIN_MEMBER
     * @apiParam {long} memberId 用户ID
     * @apiParam {int} status 1正常，2锁定
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 用户不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> setMemberStatus(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        long memberId = requestNode.findPath("memberId").asLong();
        int status = requestNode.findPath("status").asInt();
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            if (memberId < 1) return okCustomJson(CODE40001, "参数错误");
            if (status != Member.MEMBER_STATUS_LOCK && status != Member.MEMBER_STATUS_NORMAL)
                return okCustomJson(CODE40001, "参数错误");
            Member member = Member.find.byId(memberId);
            if (null == member) return okCustomJson(CODE40001, "用户不存在");
            member.setStatus(status);
            member.save();
            //将用户的缓存清掉
            deleteMemberLoginStatus(member);
            businessUtils.addOperationLog(request, admin, "锁定/解锁用户，uid:" + member.id);
            return okJSON200();
        });
    }

    private void deleteMemberLoginStatus(Member member) {
        int[] deviceType = new int[]{HttpRequestDeviceUtils.TYPE_PC, HttpRequestDeviceUtils.TYPE_MOBILE, HttpRequestDeviceUtils.TYPE_APP};
        Arrays.stream(deviceType).forEach((each) -> {
            String tokenKey = cacheUtils.getMemberTokenKey(member.id);
            String key = cacheUtils.getMemberKey(each, tokenKey);
            Optional<String> tokenCache = syncCache.getOptional(tokenKey);
            if (tokenCache.isPresent()) {
                syncCache.remove(tokenCache.get());
            }
            syncCache.remove(tokenKey);
            syncCache.remove(key);
        });

    }


    /**
     * @api {GET} /v1/tk/member_balance_log/?page=&uid=&queryType= 04流水记录
     * @apiName listUserBalanceLog
     * @apiGroup ADMIN_MEMBER
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200) {double} leftBalance 变动前的相应的用户余额
     * @apiSuccess (Success 200) {double} freezeBalance 变动前的相应的用户冻结余额
     * @apiSuccess (Success 200) {double} totalBalance 变动前的相应的用户总额
     * @apiSuccess (Success 200) {double} changeAmount 变动余额
     * @apiSuccess (Success 200) {int} transactionType  流水类型
     * @apiSuccess (Success 200) {int} moneyChangedType 变动金额类型，1是可用余额变动 2是冻结余额变动
     * @apiSuccess (Success 200) {String} description 变动理由
     * @apiSuccess (Success 200) {String} createdTime 创建时间
     */
    public CompletionStage<Result> listUserBalanceLog(int page, long uid, int queryType) {
        return CompletableFuture.supplyAsync(() -> {
            ExpressionList<BalanceLog> expressionList = BalanceLog.find.query().where()
                    .eq("itemId", CASH);
            if (uid > 0) expressionList.eq("uid", uid);
            if (queryType > 0) {
                if (queryType == BusinessConstant.QUERY_TYPE_CHARGE) {
                    expressionList.or(Expr.eq("bizType", TRANSACTION_TYPE_DEPOSIT),
                            Expr.eq("bizType", TRANSACTION_TYPE_GIVE_FOR_CHARGE));
                } else if (queryType == BusinessConstant.QUERY_TYPE_CONSUME)
                    expressionList.eq("bizType", TRANSACTION_TYPE_PLACE_ORDER);
            }
            PagedList<BalanceLog> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * BusinessConstant.PAGE_SIZE_20)
                    .setMaxRows(BusinessConstant.PAGE_SIZE_20)
                    .orderBy().desc("id")
                    .findPagedList();
            int pages = pagedList.getTotalPageCount();
            List<BalanceLog> list = pagedList.getList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/tk/customer_orders/?page=&uid= 11用户订单列表
     * @apiName listCustomerOrders
     * @apiGroup ADMIN_MEMBER
     * @apiSuccess (Success 200){int} pages 页数
     * @apiSuccess (Success 200){JsonArray} list 订单列表
     * @apiSuccess (Success 200){String} orderNo 订单编号
     * @apiSuccess (Success 200){String} totalMoney 总金额
     * @apiSuccess (Success 200){String} realPay 实付
     * @apiSuccess (Success 200){String} logisticsFee 运费
     * @apiSuccess (Success 200){int} productCount 商品数量
     * @apiSuccess (Success 200){String} statusName 状态名字
     * @apiSuccess (Success 200){String} description 描述
     * @apiSuccess (Success 200){JsonArray} detailList 商品列表
     * @apiSuccess (Success 200){string} productName 商品名字
     * @apiSuccess (Success 200){string} productImgUrl 商品图片
     * @apiSuccess (Success 200){string} skuName 商品属性描述
     */
    public CompletionStage<Result> listCustomerOrders(Http.Request request, int page, long uid) {
        return CompletableFuture.supplyAsync(() -> {
            ExpressionList<Order> expressionList = Order.find.query().where().eq("uid", uid)
                    .ge("status", Order.ORDER_STATUS_PAID);
            PagedList<Order> pagedList = expressionList.orderBy().desc("id")
                    .setFirstRow((page - 1) * PAGE_SIZE_20)
                    .setMaxRows(PAGE_SIZE_20)
                    .findPagedList();
            List<Order> list = pagedList.getList();
            //读取订单详情
            list.parallelStream().forEach((each) -> {
                List<OrderDetail> details = OrderDetail.find.query().where()
                        .eq("orderId", each.id)
                        .orderBy().asc("id").findList();
                each.detailList.addAll(details);
            });
            int pages = pagedList.getTotalPageCount();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.put("pages", pages);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }


    /**
     * @api {GET} /v1/cp/admin_members/:memberId/ 01查看管理员详情
     * @apiName getAdminMember
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} userName 用户名
     * @apiSuccess {string} realName 真名
     * @apiSuccess {double} awardPercentage 返点点数
     * @apiSuccess {String} createdTimeForShow 登录时间
     * @apiSuccess {String} lastLoginIP 登录ip
     * @apiSuccess {int} groupId 所在分组id
     * @apiSuccess {long} shopId 机构ID
     * @apiSuccess {String} avatar 头像
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMember(long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            if (memberId < 1) return okCustomJson(CODE40001, "参数错误");
            AdminMember member = AdminMember.find.byId(memberId);
            if (null == member) return okCustomJson(CODE40002, "该成员不存在");
            ObjectNode result = (ObjectNode) Json.toJson(member);
            List<GroupUser> groupUser = GroupUser.find.query().where().eq("memberId", member.id)
                    .findList();
            result.set("groups", Json.toJson(groupUser));
            result.put("code", 200);
            return ok(result);
        });
    }

    /**
     * @api {GET} /v1/cp/admin_members/ 02管理员列表
     * @apiName listAdminMembers
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {json} list
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} userName 用户名
     * @apiSuccess {string} realName 真名
     * @apiSuccess {String} groupName 岗位名
     * @apiSuccess {String} orgName 部门
     * @apiSuccess {double} awardPercentage 返点点数
     * @apiSuccess {int} groupId 组id
     * @apiSuccess {String} createdTime 登录时间
     * @apiSuccess {String} lastLoginIP 登录ip
     */
    public CompletionStage<Result> listAdminMembers() {
        return CompletableFuture.supplyAsync(() -> {
            List<AdminMember> list = AdminMember.find.query().where().orderBy().asc("id").findList();
            list.parallelStream().forEach((each) -> {
                List<GroupUser> groupUserList = GroupUser.find.query().where().eq("memberId", each.id)
                        .orderBy().asc("id")
                        .findList();
                each.groupUserList.addAll(groupUserList);
                if (each.orgId > 0 && ValidationUtil.isEmpty(each.orgName)) {
                    Org org = Org.find.byId(each.orgId);
                    if (null != org) {
                        each.setOrgName(org.name);
                        each.save();
                    }
                }
            });

            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });

    }


    /**
     * @api {POST} /v1/cp/admin_member/new/ 03添加成员
     * @apiName addAdminMember
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiParam {string} userName 用户名
     * @apiParam {string} realName 真名
     * @apiParam {string} password 密码6-20
     * @apiParam {string} [avatar] 头像地址
     * @apiParam {double} awardPercentage 返点点数
     * @apiParam {JsonArray} [groupIdList] groupId的数组
     * @apiParam {long} shopId 部门ID
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员已存在
     * @apiSuccess (Error 40003) {int} code 40003 分组不存在
     * @apiSuccess (Error 40004) {int} code 40004 该用户已在分组成员中
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> addAdminMember(Http.Request request) {
        JsonNode node = request.body().asJson();
        String ip = request.remoteAddress();
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            AdminMember member = Json.fromJson(node, AdminMember.class);
            String password = node.findPath("password").asText();
            member.setPassword(password);
            if (ValidationUtil.isEmpty(member.realName)) return okCustomJson(CODE40001, "请输入成员名字");
            if (!ValidationUtil.isValidPassword(member.password)) return okCustomJson(CODE40001, "密码6-20位");
            if (ValidationUtil.isEmpty(member.userName)) return okCustomJson(CODE40001, "请输入帐号");
            if (member.userName.length() > 20) return okCustomJson(CODE40001, "帐号最长20位");
            List<AdminMember> existMembers = AdminMember.find.query().where().eq("userName", member.userName).findList();
            if (existMembers.size() > 0) return okCustomJson(CODE40002, "该用户已存在");
            long currentTime = dateUtils.getCurrentTimeByMills();
            long orgId = node.findPath("orgId").asLong();
            if (orgId > 0) {
                Org org = Org.find.byId(orgId);
                if (null == org) return okCustomJson(CODE40001, "该店铺不存在");
                member.setOrgId(orgId);
                member.setOrgName(org.name);
            }
            String avatar = node.findPath("avatar").asText();
            if (!ValidationUtil.isEmpty(avatar)) member.setAvatar(avatar);
            String phoneNumber = node.findPath("phoneNumber").asText();
            if (!ValidationUtil.isEmpty(phoneNumber)) {
                if (!ValidationUtil.isPhoneNumber(phoneNumber)) return okCustomJson(CODE40001, "手机号码有误");
                member.setPhoneNumber(phoneNumber);
            }
            member.setPassword(encodeUtils.getMd5WithSalt(member.password));
            member.setCreatedTime(currentTime);
            member.setLastLoginIP(ip);
            member.setLastLoginTime(currentTime);
            member.setPinyinAbbr(pinyin4j.toPinYinUppercase(member.realName));
            member.save();
            saveGroup(node, member);
            businessUtils.addOperationLog(request, admin, "添加成员：" + member.toString());
            return okJSON200();
        });

    }

    /**
     * @api {POST} /v1/cp/admin_member/:id/ 04修改管理员
     * @apiName updateAdminMember
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiParam {string} [userName] 用户名
     * @apiParam {string} [realName] 真名
     * @apiParam {string} [password] 新密码6-20
     * @apiParam {string} [avatar] 头像地址
     * @apiParam {double} [awardPercentage] 返点点数
     * @apiParam {JsonArray} [groupIdList] groupId的数组
     * @apiParam {long} [shopId] 部门ID
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40001) {int} code 40002 该管理员不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> updateAdminMember(Http.Request request, long id) {
        JsonNode node = request.body().asJson();
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            AdminMember updateMember = Json.fromJson(node, AdminMember.class);
            if (null == updateMember) return okCustomJson(CODE40001, "参数错误");
            AdminMember existMember = AdminMember.find.query().where().eq("id", id).findOne();
            if (null == existMember) return okCustomJson(CODE40002, "该成员不存在");
            businessUtils.addOperationLog(request, admin, "修改权限，执行前" + existMember.toString() + ";修改的参数：" + updateMember.toString());


            if (!ValidationUtil.isEmpty(updateMember.userName) && !updateMember.userName.equals(existMember.userName))
                existMember.setUserName(updateMember.userName);
            if (!ValidationUtil.isEmpty(updateMember.realName) && !updateMember.realName.equals(existMember.realName)) {
                existMember.setRealName(updateMember.realName);
                List<GroupUser> groupUser = GroupUser.find.query().where().eq("memberId", existMember.id).findList();
                groupUser.parallelStream().forEach((each) -> {
                    each.setRealName(updateMember.realName);
                    each.save();
                });
                existMember.setPinyinAbbr(pinyin4j.toPinYinUppercase(existMember.realName));
            }
            String avatar = node.findPath("avatar").asText();
            if (!ValidationUtil.isEmpty(avatar)) {
                existMember.setAvatar(avatar);
            }
            String phoneNumber = node.findPath("phoneNumber").asText();
            if (!ValidationUtil.isEmpty(phoneNumber)) {
                if (!ValidationUtil.isPhoneNumber(phoneNumber)) return okCustomJson(CODE40001, "手机号码有误");
                existMember.setPhoneNumber(phoneNumber);
            }
            int status = node.findPath("status").asInt();
            if (status > 0) {
                existMember.setStatus(status);
            }
            
            try {
                String showOrgType = node.findPath("showOrgType").asText();
                ObjectMapper mapper = new ObjectMapper();
                int[] array = mapper.readValue(showOrgType, int[].class);
            } catch (JsonProcessingException e) {
                return okCustomJson(CODE40001, "可见商户类型格式错误");
            }
            
            try {
                String showShopPlace = node.findPath("showShopPlace").asText();
                ObjectMapper mapper = new ObjectMapper();
                int[] array = mapper.readValue(showShopPlace, int[].class);
            } catch (JsonProcessingException e) {
                return okCustomJson(CODE40001, "可见商户区域格式错误");
            }
            existMember.save();
            saveGroup(node, existMember);
            return okJSON200();
        });
    }

    private void saveGroup(JsonNode node, AdminMember existMember) {
        if (node.has("groupIdList")) {
            ArrayNode list = (ArrayNode) node.findPath("groupIdList");
            if (null != list && list.size() > 0) {
                List<GroupUser> groupUsers = new ArrayList<>();
                if (list.size() > 0) {
                    long currentTime = dateUtils.getCurrentTimeByMills();
                    list.forEach((each) -> {
                        GroupUser groupUser = new GroupUser();
                        Group group = Group.find.byId(each.asInt());
                        if (null != group) {
                            groupUser.setGroupId(group.id);
                            groupUser.setGroupName(group.groupName);
                            groupUser.setMemberId(existMember.id);
                            groupUser.setRealName(existMember.realName);
                            groupUser.setCreateTime(currentTime);
                            groupUsers.add(groupUser);
                        }
                    });
                    if (list.size() > 0) {
                        //删除旧的
                        List<GroupUser> oldGroupUser = GroupUser.find.query().where()
                                .eq("memberId", existMember.id).findList();
                        if (oldGroupUser.size() > 0) DB.deleteAll(oldGroupUser);
                        DB.saveAll(groupUsers);
                    }
                }
            }
        }
    }


    /**
     * @api {POST} /v1/cp/admin_member/ 05删除管理员
     * @apiName delAdminMember
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiParam {int} id 管理员id
     * @apiParam {String} operation 操作,"del"为删除
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 该管理员不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> delAdminMember(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            String operation = jsonNode.findPath("operation").asText();
            if (ValidationUtil.isEmpty(operation) || !operation.equals("del"))
                return okCustomJson(CODE40001, "参数错误");
            long id = jsonNode.findPath("id").asLong();
            if (id < 1) return okCustomJson(CODE40001, "参数错误");
            AdminMember member = AdminMember.find.byId(id);
            if (null == member) return okCustomJson(CODE40002, "该成员不存在");
            businessUtils.addOperationLog(request, admin, "删除成员：" + member.toString());
            member.delete();
            List<GroupUser> list = GroupUser.find.query().where().eq("memberId", id).findList();
            if (list.size() > 0) DB.deleteAll(list);
            return okJSON200();
        });
    }


    /**
     * @api {POST} /v1/cp/admin_members/status/ 07锁定/解锁管理员
     * @apiName lockMember
     * @apiGroup Admin-Member
     * @apiParam {long} memberId 用户ID
     * @apiParam {int} status 1正常，2锁定
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Error 40001){int} code 40001 用户不存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> setAdminMemberStatus(Http.Request request) {
        JsonNode requestNode = request.body().asJson();
        long memberId = requestNode.findPath("memberId").asLong();
        int status = requestNode.findPath("status").asInt();
        Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            if (memberId < 1) return okCustomJson(CODE40001, "参数错误");
            if (status != AdminMember.STATUS_NORMAL && status != AdminMember.STATUS_LOCK)
                return okCustomJson(CODE40001, "参数错误");
            AdminMember member = AdminMember.find.byId(memberId);
            if (null == member) return okCustomJson(CODE40001, "该用户不存在");
            member.setStatus(status);
            if (status == AdminMember.STATUS_NORMAL) syncCache.remove(KEY_LOGIN_MAX_ERROR_TIMES + member.id);
            member.save();
            businessUtils.addOperationLog(request, admin, "锁定/解锁成员：" + member.toString());
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v1/tk/admin_member/info/ 08查看自己详情信息
     * @apiName getAdminMemberInfo
     * @apiGroup Admin-Member
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess {int} id 用户id
     * @apiSuccess {string} name 用户名
     * @apiSuccess {string} avatar avatar
     * @apiSuccess {int} groupId 所在分组id
     * @apiSuccess {long} shopId 机构ID
     * @apiSuccess {String} avatar 头像
     * @apiSuccess (Error 40001) {int} code 40001 参数错误
     * @apiSuccess (Error 40002) {int} code 40002 该管理员不存在
     */
    public CompletionStage<Result> getAdminMemberInfo(Http.Request request) {
        Optional<AdminMember> optional = businessUtils.getAdminByAuthToken(request);
        return CompletableFuture.supplyAsync(() -> {
            if (!optional.isPresent()) return unauth403();
            AdminMember admin = optional.get();
            if (null == admin) return unauth403();
            AdminMember member = AdminMember.find.byId(admin.id);
            if (null == member) return unauth403();
            ObjectNode result = (ObjectNode) Json.toJson(member);
            result.put("code", 200);
            result.put("id", member.id);
            result.put("name", member.realName);
            result.put("avatar", member.avatar);
            result.put("orgName", member.orgName);
            result.put("orgId", member.orgId);
            result.put("introduction", "");

            List<Integer> groupIdList = new ArrayList<>();
            List<GroupUser> groupUserList = GroupUser.find.query().where()
                    .eq("memberId", admin.id)
                    .orderBy().asc("id")
                    .findList();
            List<String> roleList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            groupUserList.parallelStream().forEach((each) -> {
                        roleList.add(each.groupName);
                        sb.append(each.groupName).append(" ");
                        groupIdList.add(each.groupId);
                    }
            );
            boolean isAdmin = false;
            for (GroupUser groupUser : groupUserList) {
                Group group = Group.find.byId(groupUser.groupId);
                if (null != group) {
                    isAdmin = group.isAdmin;
                    if (isAdmin) break;
                }
            }
            result.put("isAdmin", isAdmin);
            result.put("groupName", sb.toString());
            result.set("roles", Json.toJson(roleList));
            result.set("groupIdList", Json.toJson(groupIdList));
            result.put("lastLoginTime", dateUtils.formatToYMDHMSBySecond(member.lastLoginTime));
            result.put("createdTime", dateUtils.formatToYMDHMSBySecond(member.createdTime));
            return ok(result);
        });
    }

    /**
     * @api {POST} /v1/tk/bind_member_to_group/ 09批量绑定用户到角色组
     * @apiName bindMemberToGroup
     * @apiGroup Admin-Member
     * @apiParam {long} uid 用户ID
     * @apiParam {JsonArray} list groupId的数组
     * @apiSuccess (Success 200){int} code 200
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public CompletionStage<Result> bindMemberToGroup(Http.Request request) {
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            Optional<AdminMember> adminOptional = businessUtils.getAdminByAuthToken(request);
            if (!adminOptional.isPresent()) return unauth403();
            AdminMember admin = adminOptional.get();
            long uid = jsonNode.findPath("uid").asLong();
            AdminMember member = AdminMember.find.byId(uid);
            if (null == member) return okCustomJson(CODE40001, "用户ID有误");

            ArrayNode list = (ArrayNode) jsonNode.findPath("list");
            if (null != list && list.size() > 0) {
                List<GroupUser> groupUsers = new ArrayList<>();
                if (list.size() > 0) {
                    long currentTime = dateUtils.getCurrentTimeByMills();
                    list.forEach((node) -> {
                        GroupUser groupUser = new GroupUser();
                        Group group = Group.find.byId(node.asInt());
                        if (null != group) {
                            groupUser.setGroupId(group.id);
                            groupUser.setGroupName(group.groupName);
                            groupUser.setMemberId(uid);
                            groupUser.setRealName(member.realName);
                            groupUser.setCreateTime(currentTime);
                            groupUsers.add(groupUser);
                        }
                    });
                    if (list.size() > 0) {
                        //删除旧的
                        List<GroupUser> oldGroupUser = GroupUser.find.query().where()
                                .eq("memberId", member.id).findList();
                        if (oldGroupUser.size() > 0) DB.deleteAll(oldGroupUser);
                        DB.saveAll(groupUsers);
                    }
                }
            }
            businessUtils.addOperationLog(request, admin, "批量修改用户角色：" + jsonNode.toString());
            return okJSON200();
        });
    }

    /**
     * @api {GET} /v1/tk/user_groups/?memberId= 10用户所属分组
     * @apiName listUserGroups
     * @apiGroup Admin-Member
     * @apiSuccess {json} list
     * @apiSuccess (Success 200) {int} code 200 请求成功
     */
    public CompletionStage<Result> listUserGroups(Http.Request request, long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            List<GroupUser> list = GroupUser.find.query().where().eq("memberId", memberId).findList();
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list", Json.toJson(list));
            return ok(node);
        });
    }


}
