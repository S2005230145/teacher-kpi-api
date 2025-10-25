package models.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;

/**
 * 订单详情
 */
@Entity
@Table(name = "v1_order_detail")
public class OrderDetail extends Model {
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_APPLY_RETURN = 1;
    public static final int STATUS_AGREE_RETURN = 2;
    public static final int STATUS_REFUND = 3;
    public static final int STATUS_DISAGREE_RETURN = -1;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "uid")
    public long uid;

    @Column(name = "product_id")
    public long productId;

    @Column(name = "product_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productName;

    @Column(name = "category_id")
    public long categoryId;

    @Column(name = "category_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String categoryName = "";

    @Column(name = "purchasing_category_id")
    public long purchasingCategoryId;

    @Column(name = "purchasing_category_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String purchasingCategoryName = "";

    @Column(name = "old_price")
    public long oldPrice;

    @Column(name = "weight")
    public double weight;

    @Column(name = "product_price")
    public long productPrice;

    @Column(name = "purchasing_price")
    public long purchasingPrice;

    @Column(name = "result_price")
    public long resultPrice;

    @Column(name = "sku_id")
    public long productSkuId;

    @Column(name = "sku_type")
    public int skuType;

    @Column(name = "sku_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String skuName;

    @Column(name = "unit")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String unit;

    @Column(name = "product_img_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productImgUrl;

    @Column(name = "product_mode_desc")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productModeDesc;//商品型号信息

    @Column(name = "product_mode_params")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String productModeParams;//商品型号参数

    @Column(name = "discount_rate")
    public int discountRate;//折扣比例,95折，就写95

    @Column(name = "discount_amount")
    public long discountAmount;//折扣金额

    @Column(name = "number")
    public long number;//购买数量

    @Column(name = "storage_number")
    public long storageNumber;

    @Column(name = "sub_total")
    public long subTotal;//小计金额

    @Column(name = "already_delivery_number")
    public long alreadyDeliveryNumber;

    @Column(name = "is_product_available")
    public boolean isProductAvailable;//商品是否有效，0有效，1失效

    @Column(name = "exclude_discount")
    public boolean excludeDiscount;

    @Column(name = "remark")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String remark;//备注

    @Column(name = "source")
    public int source;

    @Column(name = "delivery_method")
    public int deliveryMethod;

    @Column(name = "return_status")
    public long returnStatus;

    @Column(name = "sub_return")
    public long subReturn;

    @Column(name = "return_number")
    public long returnNumber;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "take_time")
    public long takeTime;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "sku_quantity")
    public long skuQuantity;

    @Column(name = "staff_id")
    public long staffId;

    @Column(name = "status")
    public long status;

    @Column(name = "supplier_id")
    @JsonIgnore
    public long supplierId;

    @Column(name = "bid_price")
    @JsonIgnore
    public long bidPrice;

    @Transient
    public long brandId;

    @Transient
    public boolean joinFlashSale;

    @Transient
    public int productType;

    @Transient
    public int stockMode;

    @Transient
    public boolean needExtraTimeToMake;

    @Transient
    public String barcode;

    public static Finder<Long, OrderDetail> find = new Finder<>(OrderDetail.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getPurchasingCategoryId() {
        return purchasingCategoryId;
    }

    public void setPurchasingCategoryId(long purchasingCategoryId) {
        this.purchasingCategoryId = purchasingCategoryId;
    }

    public String getPurchasingCategoryName() {
        return purchasingCategoryName;
    }

    public void setPurchasingCategoryName(String purchasingCategoryName) {
        this.purchasingCategoryName = purchasingCategoryName;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getPurchasingPrice() {
        return purchasingPrice;
    }

    public void setPurchasingPrice(long purchasingPrice) {
        this.purchasingPrice = purchasingPrice;
    }

    public long getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(long productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getProductModeDesc() {
        return productModeDesc;
    }

    public void setProductModeDesc(String productModeDesc) {
        this.productModeDesc = productModeDesc;
    }

    public String getProductModeParams() {
        return productModeParams;
    }

    public void setProductModeParams(String productModeParams) {
        this.productModeParams = productModeParams;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(long subTotal) {
        this.subTotal = subTotal;
    }

    public long getAlreadyDeliveryNumber() {
        return alreadyDeliveryNumber;
    }

    public void setAlreadyDeliveryNumber(long alreadyDeliveryNumber) {
        this.alreadyDeliveryNumber = alreadyDeliveryNumber;
    }

    public boolean isProductAvailable() {
        return isProductAvailable;
    }

    public void setProductAvailable(boolean productAvailable) {
        isProductAvailable = productAvailable;
    }

    public boolean isExcludeDiscount() {
        return excludeDiscount;
    }

    public String getRemark() {
        return remark;
    }

    public void setExcludeDiscount(boolean excludeDiscount) {
        this.excludeDiscount = excludeDiscount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public long getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(long returnStatus) {
        this.returnStatus = returnStatus;
    }

    public long getSubReturn() {
        return subReturn;
    }

    public void setSubReturn(long subReturn) {
        this.subReturn = subReturn;
    }

    public long getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(long returnNumber) {
        this.returnNumber = returnNumber;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(long takeTime) {
        this.takeTime = takeTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public boolean isJoinFlashSale() {
        return joinFlashSale;
    }

    public void setJoinFlashSale(boolean joinFlashSale) {
        this.joinFlashSale = joinFlashSale;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getStockMode() {
        return stockMode;
    }

    public void setStockMode(int stockMode) {
        this.stockMode = stockMode;
    }

    public long getResultPrice() {
        return resultPrice;
    }

    public void setResultPrice(long resultPrice) {
        this.resultPrice = resultPrice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSkuType() {
        return skuType;
    }

    public void setSkuType(int skuType) {
        this.skuType = skuType;
    }

    public boolean isNeedExtraTimeToMake() {
        return needExtraTimeToMake;
    }

    public void setNeedExtraTimeToMake(boolean needExtraTimeToMake) {
        this.needExtraTimeToMake = needExtraTimeToMake;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(long skuQuantity) {
        this.skuQuantity = skuQuantity;
    }

    public long getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(long storageNumber) {
        this.storageNumber = storageNumber;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }
}
