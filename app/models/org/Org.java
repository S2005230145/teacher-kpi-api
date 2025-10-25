package models.org;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import myannotation.EscapeHtmlSerializer;
import play.data.validation.Constraints;

@Entity
@Table(name = "v1_org")
public class Org extends Model {
    //状态，1为正常，2为被锁定，3为待审核 4审核不通过 5审核驳回
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_LOCK = 2;
    public static final int STATUS_NEED_APPROVE = 3;
    public static final int STATUS_DENY = 4;
    public static final int STATUS_RETURN = 5;
    public static final int STATUS_DELETED = 6;

    public static final int DELIVERY_TYPE_SELF = 1;     //自配送
    public static final int DELIVERY_TYPE_DRIVER = 2; //骑手配送
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "status")
    public int status;

    @Column(name = "area_id")
    public long areaId;


    @Column(name = "score")
    public int score;


    @Column(name = "sold_amount")
    public long soldAmount;

    @Column(name = "min_to_send")
    public int minToSend;

    @Column(name = "delivery_target")
    public String deliveryTarget;

    @Column(name = "tag")
    public String tag;

    @Column(name = "area_name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String areaName;


    @Column(name = "name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "digest")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest;

    @Column(name = "contact_number")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactNumber;//联系电话

    @Column(name = "contact_name")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactName;//联系人

    @Column(name = "contact_address")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactAddress;//联系地址

    @Column(name = "license_number")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseNumber;//营业执照号

    @Column(name = "license_img")
    @Constraints.Required
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String licenseImg;//营业执照图片

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;//备注

    @Column(name = "shop_amount")
    public long shopAmount;

    @Column(name = "shop_max_amount")
    public long shopMaxAmount;

    @Column(name = "sort")
    public long sort;

    @Column(name = "create_uid")
    public long createUid;

    @Column(name = "create_name")
    public long createName;

    @Column(name = "charge_time")
    public long chargeTime;

    @Column(name = "renew_time")
    public long renewTime;

    @Column(name = "update_time")
    public long updateTime;//更新时间

    @Column(name = "create_time")
    public long createTime;//创建时间

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;//审请记录

    @Column(name = "avatar")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String avatar;

    @Column(name = "rect_logo")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String rectLogo;

    @Column(name = "company_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String companyName;

    @Column(name = "company_type")
    public int companyType;

    @Column(name = "license_type")
    public int licenseType;

    @Column(name = "business_items")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String businessItems;

    @Column(name = "open_time")
    public int openTime;

    @Column(name = "close_time")
    public int closeTime;

    @Column(name = "open_time_minute")
    public int openTimeMinute;

    @Column(name = "close_time_minute")
    public int closeTimeMinute;

    @Column(name = "shop_place_id")
    public long shopPlaceId;

    @Column(name = "lat")
    public double lat;

    @Column(name = "lon")
    public double lon;

    @Column(name = "shop_place_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopPlaceName;

    @Column(name = "floor")
    public String floor;
    @Column(name = "recommend")
    public int recommend;

    @Column(name = "packing_fee")
    public int packingFee;

    @Column(name = "packing_box_price")
    public long packingBoxPrice;

    @Column(name = "delivery_fee")
    public int deliveryFee;

    @Column(name = "time_to_deliver")
    public long timeToDeliver;

    @Column(name = "min_deliver_fee")
    public int minDeliverFee;

    @Column(name = "announcement") //店铺公告
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String announcement;

    @Column(name = "enable") //(店铺开关(0关,1开))
    public int enable;

    @Column(name = "bank_name") //(银行))
    public String bankName;

    @Column(name = "bank_account_name") //(账户名))
    public String bankAccountName;

    @Column(name = "bank_card") //(银行卡))
    public String bankCard;

    @Column(name = "commission_percent") //(佣金百分比))
    public double commissionPercent;

    @Column(name = "delivery_type") //配送方式（1自配送，2骑手配送）
    public int deliveryType;

    @Column(name = "service_fee")
    public long serviceFee;  //服务费

    @Column(name = "same_district_deliver_fee")
    public long sameDistrictDeliverFee;

    @Column(name = "diff_district_deliver_fee")
    public long diffDistrictDeliverFee;

    @Column(name = "receiver_id")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String receiverId;

    @Column(name = "profit_share")
    public boolean profitShare;

    public static Finder<Long, Org> find = new Finder<>(Org.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(long soldAmount) {
        this.soldAmount = soldAmount;
    }

    public int getMinToSend() {
        return minToSend;
    }

    public void setMinToSend(int minToSend) {
        this.minToSend = minToSend;
    }

    public String getDeliveryTarget() {
        return deliveryTarget;
    }

    public void setDeliveryTarget(String deliveryTarget) {
        this.deliveryTarget = deliveryTarget;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(long shopAmount) {
        this.shopAmount = shopAmount;
    }

    public long getShopMaxAmount() {
        return shopMaxAmount;
    }

    public void setShopMaxAmount(long shopMaxAmount) {
        this.shopMaxAmount = shopMaxAmount;
    }

    public long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(long createUid) {
        this.createUid = createUid;
    }

    public long getCreateName() {
        return createName;
    }

    public void setCreateName(long createName) {
        this.createName = createName;
    }

    public long getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(long chargeTime) {
        this.chargeTime = chargeTime;
    }

    public long getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(long renewTime) {
        this.renewTime = renewTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRectLogo() {
        return rectLogo;
    }

    public void setRectLogo(String rectLogo) {
        this.rectLogo = rectLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public String getBusinessItems() {
        return businessItems;
    }

    public void setBusinessItems(String businessItems) {
        this.businessItems = businessItems;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public int getOpenTimeMinute() {
        return openTimeMinute;
    }

    public void setOpenTimeMinute(int openTimeMinute) {
        this.openTimeMinute = openTimeMinute;
    }

    public int getCloseTimeMinute() {
        return closeTimeMinute;
    }

    public void setCloseTimeMinute(int closeTimeMinute) {
        this.closeTimeMinute = closeTimeMinute;
    }

    public long getShopPlaceId() {
        return shopPlaceId;
    }

    public void setShopPlaceId(long shopPlaceId) {
        this.shopPlaceId = shopPlaceId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getShopPlaceName() {
        return shopPlaceName;
    }

    public void setShopPlaceName(String shopPlaceName) {
        this.shopPlaceName = shopPlaceName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getPackingFee() {
        return packingFee;
    }

    public void setPackingFee(int packingFee) {
        this.packingFee = packingFee;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public long getTimeToDeliver() {
        return timeToDeliver;
    }

    public void setTimeToDeliver(long timeToDeliver) {
        this.timeToDeliver = timeToDeliver;
    }

    public int getMinDeliverFee() {
        return minDeliverFee;
    }

    public void setMinDeliverFee(int minDeliverFee) {
        this.minDeliverFee = minDeliverFee;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public double getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(double commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public long getPackingBoxPrice() {
        return packingBoxPrice;
    }

    public void setPackingBoxPrice(long packingBoxPrice) {
        this.packingBoxPrice = packingBoxPrice;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public long getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(long serviceFee) {
        this.serviceFee = serviceFee;
    }

    public long getSameDistrictDeliverFee() {
        return sameDistrictDeliverFee;
    }

    public void setSameDistrictDeliverFee(long sameDistrictDeliverFee) {
        this.sameDistrictDeliverFee = sameDistrictDeliverFee;
    }

    public long getDiffDistrictDeliverFee() {
        return diffDistrictDeliverFee;
    }

    public void setDiffDistrictDeliverFee(long diffDistrictDeliverFee) {
        this.diffDistrictDeliverFee = diffDistrictDeliverFee;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public boolean isProfitShare() {
        return profitShare;
    }

    public void setProfitShare(boolean profitShare) {
        this.profitShare = profitShare;
    }
}
