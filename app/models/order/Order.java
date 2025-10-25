package models.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import models.org.Org;
import models.shop.Shop;
import myannotation.EscapeHtmlSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
@Entity
@Table(name = "v1_order")
public class Order extends Model {
    //订单状态 10待付款 11已支付 20待制作 30待配送 40骑手已接单 42骑手已取餐　
    //  50已送达 60待评价 -10取消交易 -20支付超时关闭 -30系统强制取消 -50售后完成 -51完成退款 -52完成换货 -53完成退货

    public static int ORDER_STATUS_UNPAY = 10;
    public static int ORDER_STATUS_PAID = 11;
    public static int ORDER_STATUS_MONTH_PAY = 12;
    public static int ORDER_STATUS_TO_MAKE = 20;
    public static int ORDER_STATUS_TO_TAKE = 30;
    public static int ORDER_STATUS_TO_DELIVER = 40;
    public static int ORDER_STATUS_PICKED = 42;
    public static int ORDER_STATUS_DELIVERED = 50;

    public static int ORDER_STATUS_PUT_MEAL = 51;  //取餐柜存餐
    public static int ORDER_STATUS_TAKE_MEAL = 52;  //取餐柜取餐
    public static int ORDER_STATUS_COMMENTED = 60;
    public static int ORDER_STATUS_CANCELED = -10;
    public static int ORDER_STATUS_PAY_OVER_TIME_CLOSE = -20;
    public static int ORDER_STATUS_SYSTEM_CANCELED = -30;
    public static int ORDER_STATUS_POST_SERVICE_FINISH = -50;
    public static int ORDER_STATUS_POST_SERVICE_FINISH_REFUND = -51;
    public static int ORDER_STATUS_POST_SERVICE_FINISH_CHANGE = -52;
    public static int ORDER_STATUS_POST_SERVICE_FINISH_RETURN = -53;

    public static final int DELIVERY_TYPE_PICK_UP = 1;
    public static final int DELIVERY_TYPE_DELIVERY = 2;
    public static final int DELIVERY_TYPE_DINE_IN = 3;
    public static final int DELIVERY_TYPE_MAIL = 4;

    public static final int ORDER_TYPE_NORMAL = 1;
    public static final int ORDER_TYPE_SERVICE = 2;
    public static final int ORDER_TYPE_MEMBERSHIP = 3;
    public static final int ORDER_TYPE_LIVE_MAKE = 4;

    public static final int ORDER_TYPE_ENROLL = 5;
    public static final int ORDER_TYPE_THIRD_DISCOUNT = 6;
    public static final int ORDER_TYPE_SCORE = 10;
    public static final int ORDER_ACTIVITY_TYPE_BARGAIN = 20;
    public static final int ORDER_TYPE_PURCHASING = 30;

    public static final int ACCOUNT_SETTLE_NOT_NEED = 0;
    public static final int ACCOUNT_SETTLE_NEED = 1;
    public static final int ACCOUNT_SETTLE_FINISHED = 2;

    public static final int PRINT_STATUS_NOT_NEED_PRINT = 1;
    public static final int PRINT_STATUS_NEED_PRINT = 2;
    public static final int PRINT_STATUS_HAS_PRINT = 3;
    public static final int PRINT_STATUS_PRINT_ERROR = -1;

    public static final int ORDER_COUNT_STATUS_NO_COUNT = 1;
    public static final int ORDER_COUNT_STATUS_COUNT = 2;

    public static int MP_SHIP_STATUS_TO_UPLOAD = 10;
    public static int MP_SHIP_STATUS_UPLOADED = 20;
    public static int MP_SHIP_STATUS_PROMPT = 30;

    public static int SOURCE_POS = 1;
    public static int SOURCE_MINI_APP = 2;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "area_id")
    public long areaId;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "org_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orgName;

    @Column(name = "uid")
    public long uid;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName;

    @Column(name = "order_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orderNo;

    @Column(name = "status")
    public int status;//订单状态 0未付款,1已付款,2已发货,3已签收,-1退货申请,-2退货中,-3已退货,-4取消交易 -5撤销申请
    @Column(name = "post_service_status")
    public int postServiceStatus;//售后状态 0 未发起售后 1 申请售后 -1 售后已取消 2 处理中 200 处理完毕

    @Column(name = "mp_status")
    public int mpStatus;// 10 20

    @Column(name = "product_count")
    public int productCount;//商品数量

    @Column(name = "total_money")
    public long totalMoney;//总价

    @Column(name = "total_return_money")
    public long totalReturnMoney;

    @Column(name = "total_return_number")
    public long totalReturnNumber;

    @Column(name = "real_pay")
    public long realPay;

    @Column(name = "discount_money")
    public long discountMoney;

    @Column(name = "mail_fee")
    public long deliverFee;

    @Column(name = "packing_fee")
    public long packingFee;

    @Column(name = "pay_method")
    public int payMethod;//支付渠道

    @Column(name = "print_count")
    public int printCount;

    @Column(name = "pay_method_name")
    public String payMethodName;

    @Column(name = "barcode")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String barcode;

    @Column(name = "pickup_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String pickupCode;


    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "pay_time")
    public long payTime;

    @Column(name = "create_time")
    public long createTime;


    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note = "";

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;

    @Column(name = "errand_status")
    public int errandStatus;//跑腿状态

    @Column(name = "print_status")
    public int printStatus;//打印状态
    @Column(name = "delivery_type")
    public int deliveryType;//配送类型

    @Column(name = "schedule_time")
    public long scheduleTime;

    @Column(name = "order_type")
    public int orderType;

    @Column(name = "paid_up")
    public long paidUp;

    @Column(name = "description")
    public String description;

    @Column(name = "filter")
    public String filter;

    @Column(name = "driver_take_task_time")
    public long driverTakeTaskTime;

    @Column(name = "driver_pickup_time")
    public long driverPickupTime;

    @Column(name = "driver_delivered_time")
    public long driverDeliveredTime;

    @Column(name = "shop_place_id")
    public long shopPlaceId;

    @Column(name = "shop_place")
    public String shopPlace;

    @Column(name = "storage_place_Id")
    public long storagePlaceId;

    @Column(name = "storage_place")
    public String storagePlace;

    @Column(name = "cabinet_code") //开柜码
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String cabinetCode;

    @Column(name = "cabinet_id")  //柜号
    public long cabinetId;

    @Column(name = "cabinet_box")  //箱号
    public int cabinetBox;

    @Column(name = "packing_box_use")  //打包盒消耗的个数
    public int packingBoxUse;

    @Column(name = "packing_box_money")  //订单打包费中打包盒的金额
    public long packingBoxMoney;

    @Column(name = "packing_bag_money") //订单打包费中打包袋的金额
    public long packingBagMoney;

    @Column(name = "packing_box_price") //单个打包盒的价格
    public long packingBoxPrice;

    @Transient
    public List<OrderDetail> detailList = new ArrayList();

    @Transient
    public Org org;

    public static Finder<Long, Order> find = new Finder<>(Order.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPostServiceStatus() {
        return postServiceStatus;
    }

    public void setPostServiceStatus(int postServiceStatus) {
        this.postServiceStatus = postServiceStatus;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public long getTotalReturnMoney() {
        return totalReturnMoney;
    }

    public void setTotalReturnMoney(long totalReturnMoney) {
        this.totalReturnMoney = totalReturnMoney;
    }

    public long getTotalReturnNumber() {
        return totalReturnNumber;
    }

    public void setTotalReturnNumber(long totalReturnNumber) {
        this.totalReturnNumber = totalReturnNumber;
    }

    public long getRealPay() {
        return realPay;
    }

    public void setRealPay(long realPay) {
        this.realPay = realPay;
    }

    public long getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(long discountMoney) {
        this.discountMoney = discountMoney;
    }

    public long getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(long deliverFee) {
        this.deliverFee = deliverFee;
    }

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public int getErrandStatus() {
        return errandStatus;
    }

    public void setErrandStatus(int errandStatus) {
        this.errandStatus = errandStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getPaidUp() {
        return paidUp;
    }

    public void setPaidUp(long paidUp) {
        this.paidUp = paidUp;
    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getMpStatus() {
        return mpStatus;
    }

    public void setMpStatus(int mpStatus) {
        this.mpStatus = mpStatus;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public long getPackingFee() {
        return packingFee;
    }

    public void setPackingFee(long packingFee) {
        this.packingFee = packingFee;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public long getShopPlaceId() {
        return shopPlaceId;
    }

    public void setShopPlaceId(long shopPlaceId) {
        this.shopPlaceId = shopPlaceId;
    }

    public String getShopPlace() {
        return shopPlace;
    }

    public void setShopPlace(String shopPlace) {
        this.shopPlace = shopPlace;
    }

    public long getStoragePlaceId() {
        return storagePlaceId;
    }

    public void setStoragePlaceId(long storagePlaceId) {
        this.storagePlaceId = storagePlaceId;
    }

    public String getStoragePlace() {
        return storagePlace;
    }

    public void setStoragePlace(String storagePlace) {
        this.storagePlace = storagePlace;
    }

    public String getCabinetCode() {
        return cabinetCode;
    }

    public void setCabinetCode(String cabinetCode) {
        this.cabinetCode = cabinetCode;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(long cabinetId) {
        this.cabinetId = cabinetId;
    }

    public int getCabinetBox() {
        return cabinetBox;
    }

    public void setCabinetBox(int cabinetBox) {
        this.cabinetBox = cabinetBox;
    }

    public int getPackingBoxUse() {
        return packingBoxUse;
    }

    public void setPackingBoxUse(int packingBoxUse) {
        this.packingBoxUse = packingBoxUse;
    }

    public long getPackingBoxMoney() {
        return packingBoxMoney;
    }

    public void setPackingBoxMoney(long packingBoxMoney) {
        this.packingBoxMoney = packingBoxMoney;
    }

    public long getPackingBagMoney() {
        return packingBagMoney;
    }

    public void setPackingBagMoney(long packingBagMoney) {
        this.packingBagMoney = packingBagMoney;
    }

    public long getPackingBoxPrice() {
        return packingBoxPrice;
    }

    public void setPackingBoxPrice(long packingBoxPrice) {
        this.packingBoxPrice = packingBoxPrice;
    }
}

