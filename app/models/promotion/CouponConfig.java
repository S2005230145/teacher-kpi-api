package models.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 */
@Entity
@Table(name = "v1_coupon_config")
public class CouponConfig extends Model {
    //1生效，2失效
    public static final int STATUS_ENABLE = 1;
    public static final int STATUS_DISABLE = 2;
    
    //1全场，2指定ID
    public static final int ID_TYPE_ALL = 1;
    public static final int ID_TYPE_SPECIFIED_ID = 2;
    
    //1为全场景使用，2为注册用, 4为弹窗用 5为弹窗不生成记录
    public static final int TYPE_ALL_CAN_USE = 1;
    public static final int TYPE_REG_USE = 2;
    public static final int TYPE_POP_UP = 4;
    public static final int TYPE_POP_UP_NOT_NEED_GENERATE_RECORD = 5;
    
    public static final int TYPE_ONE_EACH_DAY = 6;
    
    public static final int SOURCE_PLATFORM = 10;
    public static final int SOURCE_SHOP = 20;
    
    
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    
    @Column(name = "coupon_title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String couponTitle;//标题
    
    @Column(name = "coupon_content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String couponContent;//内容
    
    @Column(name = "org_id")
    public long orgId;
    
    
    @Column(name = "area_id")
    public long areaId;
    
    @Column(name = "amount")
    public int amount;//面值，以分为单位
    
    @Column(name = "type")
    public int type;//类型
    
    @Column(name = "status")
    public int status;//1生效，2失效
    
    @Column(name = "need_show")
    public boolean needShow;
    
    @Column(name = "claim_per_member")
    public int claimLimitPerMember;//每人限领张数
    
    @Column(name = "total_amount")
    public int totalAmount;//总数
    
    @Column(name = "claim_amount")
    public int claimAmount;//已认领数量
    
    @Column(name = "id_type")
    public int idType;//ID类型,1全场，2分类，3商品ID
    
    @Column(name = "rule_content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String ruleContent;
    
    @Column(name = "type_rules")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String typeRules;
    
    @Column(name = "use_rules")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String useRules;
    
    @Column(name = "upto")
    public long upto;
    
    @Column(name = "free")
    public long free;
    
    @Column(name = "merchant_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String merchantIds;//适用商品ID，以逗号隔开
    
    @Column(name = "shop_category_ids")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopCategoryIds;
    
    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//图片
    
    @Column(name = "begin_use_time")
    public long beginUseTime;
    
    @Column(name = "begin_time")
    public long beginTime;//生效时间
    
    @Column(name = "end_time")
    public long endTime;//失效时间
    
    @Column(name = "expire_days")
    public long expireDays;//有效期天数
    
    @Column(name = "expire_time")
    public long expireTime;
    
    @Column(name = "old_price")
    public int oldPrice;//原价
    
    @Column(name = "current_price")
    public int currentPrice;//现价
    
    @Column(name = "update_time")
    public long updateTime;
    
    @Column(name = "source")
    public int source;
    
    @Column(name = "combo_coupon_id_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String comboCouponIdList;
    
    @Column(name = "combo_coupon_title_list")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String comboCouponTitleList;
    
    @Transient
    public boolean available;
    
    @Transient
    public List<CouponConfig> comboCouponList = new ArrayList<>();
    
    @Transient
    public String title;
    
    public static Finder<Long, CouponConfig> find = new Finder<>(CouponConfig.class);
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getCouponTitle() {
        return couponTitle;
    }
    
    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }
    
    public String getCouponContent() {
        return couponContent;
    }
    
    public void setCouponContent(String couponContent) {
        this.couponContent = couponContent;
    }
    
    public long getOrgId() {
        return orgId;
    }
    
    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public boolean isNeedShow() {
        return needShow;
    }
    
    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }
    
    public int getClaimLimitPerMember() {
        return claimLimitPerMember;
    }
    
    public void setClaimLimitPerMember(int claimLimitPerMember) {
        this.claimLimitPerMember = claimLimitPerMember;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getClaimAmount() {
        return claimAmount;
    }
    
    public void setClaimAmount(int claimAmount) {
        this.claimAmount = claimAmount;
    }
    
    public int getIdType() {
        return idType;
    }
    
    public void setIdType(int idType) {
        this.idType = idType;
    }
    
    public String getRuleContent() {
        return ruleContent;
    }
    
    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }
    
    public String getTypeRules() {
        return typeRules;
    }
    
    public void setTypeRules(String typeRules) {
        this.typeRules = typeRules;
    }
    
    public String getUseRules() {
        return useRules;
    }
    
    public void setUseRules(String useRules) {
        this.useRules = useRules;
    }
    
    public long getUpto() {
        return upto;
    }
    
    public void setUpto(long upto) {
        this.upto = upto;
    }
    
    public long getFree() {
        return free;
    }
    
    public void setFree(long free) {
        this.free = free;
    }
    
    public String getMerchantIds() {
        return merchantIds;
    }
    
    public void setMerchantIds(String merchantIds) {
        this.merchantIds = merchantIds;
    }
    
    public String getShopCategoryIds() {
        return shopCategoryIds;
    }
    
    public void setShopCategoryIds(String shopCategoryIds) {
        this.shopCategoryIds = shopCategoryIds;
    }
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    public long getBeginUseTime() {
        return beginUseTime;
    }
    
    public void setBeginUseTime(long beginUseTime) {
        this.beginUseTime = beginUseTime;
    }
    
    public long getBeginTime() {
        return beginTime;
    }
    
    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public long getExpireDays() {
        return expireDays;
    }
    
    public void setExpireDays(long expireDays) {
        this.expireDays = expireDays;
    }
    
    public long getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    public int getOldPrice() {
        return oldPrice;
    }
    
    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }
    
    public int getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getComboCouponIdList() {
        return comboCouponIdList;
    }
    
    public void setComboCouponIdList(String comboCouponIdList) {
        this.comboCouponIdList = comboCouponIdList;
    }
    
    public String getComboCouponTitleList() {
        return comboCouponTitleList;
    }
    
    public void setComboCouponTitleList(String comboCouponTitleList) {
        this.comboCouponTitleList = comboCouponTitleList;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public long getAreaId() {
        return areaId;
    }
    
    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }
    
    public int getSource() {
        return source;
    }
    
    public void setSource(int source) {
        this.source = source;
    }
}

