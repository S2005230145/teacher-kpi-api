package models.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import models.shop.Shop;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;


@Entity
@Table(name = "v1_card_coupon_config")
public class CardCouponConfig extends Model {

    public static final int STATUS_NOT_BEGIN = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_END = -1;

    public static final int CARD_TYPE_NORMAL = 10;
    public static final int CARD_TYPE_ONE_EACH_DAY = 20;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;

    @Column(name = "digest")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest;

    @Column(name = "img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String imgUrl;//图片

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "contact_phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactPhoneNumber;

    @Column(name = "contact_address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String contactAddress;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "days")
    public long days;

    @Column(name = "expire_time")
    public long expireTime;

    @Column(name = "no_limit_count")
    public boolean noLimitCount;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "sku_id")
    public long skuId;

    @Column(name = "lat")
    public double lat;

    @Column(name = "lng")
    public double lng;

    @Column(name = "give_count")
    public int giveCount;

    @Column(name = "status")
    public int status;

    @Column(name = "card_type")
    public int cardType;

    @Column(name = "discount")
    public long discount;

    @Column(name = "price")
    public long price;

    @Column(name = "begin_use_time")
    public long beginUseTime;

    @Column(name = "only_pick_up")
    public boolean onlyPickUp;

    @Transient
    public Shop shop;

    @Transient
    public String productName;

    @Transient
    public String skuName;


    public static Finder<Long, CardCouponConfig> find = new Finder<>(CardCouponConfig.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public boolean isNoLimitCount() {
        return noLimitCount;
    }

    public void setNoLimitCount(boolean noLimitCount) {
        this.noLimitCount = noLimitCount;
    }

    public int getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }


    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public boolean isOnlyPickUp() {
        return onlyPickUp;
    }

    public void setOnlyPickUp(boolean onlyPickUp) {
        this.onlyPickUp = onlyPickUp;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBeginUseTime() {
        return beginUseTime;
    }

    public void setBeginUseTime(long beginUseTime) {
        this.beginUseTime = beginUseTime;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}

