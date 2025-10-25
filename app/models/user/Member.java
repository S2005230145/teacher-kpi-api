package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * Created by win7 on 2016/6/7.
 */
@Entity
@Table(name = "v1_member")
public class Member extends Model {
    private static final long serialVersionUID = 8880942309217592333L;
    public static final int MEMBER_TYPE_NORMAL = 1;
    public static final int MEMBER_TYPE_ANONYMOUS = 2;
    /**
     * 用户的状态：正常
     */
    public static final int MEMBER_STATUS_NORMAL = 1;
    /**
     * 用户的状态：被锁定
     */
    public static final int MEMBER_STATUS_LOCK = 2;
    //1普通会员，2高级会员，3钻石会员，4至尊会员
    public static final int LEVEL_0 = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;

    public static final int AUTH_STATUS_DENY = -1;
    public static final int AUTH_STATUS_NOT_AUTH = 0;
    public static final int AUTH_STATUS_PROCESSING = 1;
    public static final int AUTH_STATUS_PRE_AUTH = 2;
    public static final int AUTH_STATUS_AUTHED = 3;

    public static final int USER_TYPE_NORMAL = 1;//用户
    public static final int USER_TYPE_SALESMAN = 10;//营业员
    public static final int USER_TYPE_MANAGER = 20;//管理员
    public static final int USER_TYPE_BOSS = 30;//BOSS


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(30)
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(name = "login_password")
    public String loginPassword;//登录密码

    @Column(name = "pay_password")
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String payPassword;//支付密码

    @Column(name = "status")
    public int status;//用户状态 1正常2锁定

    @Column(name = "real_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String realName;//真实姓名

    @Column(name = "nick_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String nickName;//昵称

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;//手机号

    @Column(name = "contact_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactNumber;

    @Column(name = "create_time")
    public long createdTime;//创建时间

    @Column(name = "dealer_id")
    public long dealerId;

    @Column(name = "dealer_type")
    public long dealerType;

    @Column(name = "level")
    public int level;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "shop_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopName;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;//头像

    @Column(name = "user_type")
    public int userType;
    @Column(name = "member_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String memberNo;//会员码
    @Column(name = "birthday")
    public long birthday;

    @Column(name = "birthday_month")
    public int birthdayMonth;

    @Column(name = "birthday_day")
    public int birthdayDay;
    @Column(name = "barcode_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcodeImgUrl;

    @Column(name = "open_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String openId;

    @Column(name = "session_key")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String sessionKey;

    @Column(name = "union_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String unionId;

    @Column(name = "id_card_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String idCardNo;//身份证号码
    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "user_note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userNote;

    @Column(name = "inside_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String insideNumber;

    @Column(name = "dealer_level")
    public long dealerLevel;

    @Column(name = "sex")
    public int sex;
    @Transient
    public long leftBalance;

    @Transient
    public long score;

    @Transient
    public String dealerName;

    @Transient
    public double totalOrderMoney;
    @Transient
    public String levelName;
    @Transient
    public int orderDiscount;

    @Transient
    public List<MemberBalance> balances = new ArrayList();

    public static Finder<Long, Member> find = new Finder<>(Member.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getDealerType() {
        return dealerType;
    }

    public void setDealerType(long dealerType) {
        this.dealerType = dealerType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public double getTotalOrderMoney() {
        return totalOrderMoney;
    }

    public void setTotalOrderMoney(double totalOrderMoney) {
        this.totalOrderMoney = totalOrderMoney;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(int birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public int getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(int birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getBarcodeImgUrl() {
        return barcodeImgUrl;
    }

    public void setBarcodeImgUrl(String barcodeImgUrl) {
        this.barcodeImgUrl = barcodeImgUrl;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public long getDealerLevel() {
        return dealerLevel;
    }

    public void setDealerLevel(long dealerLevel) {
        this.dealerLevel = dealerLevel;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(int orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public String getInsideNumber() {
        return insideNumber;
    }

    public void setInsideNumber(String insideNumber) {
        this.insideNumber = insideNumber;
    }
}
