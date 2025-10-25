package models.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;

/**
 * 收货信息
 */
@Entity
@Table(name = "v1_order_receive_detail")
public class OrderReceiveDetail extends Model {

    public static final int DELIVERY_TYPE_PICK_UP = 1;
    public static final int DELIVERY_TYPE_DELIVERY = 2;
    public static final int DELIVERY_TYPE_DINE_IN = 3;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "uid")
    public long uid;

    @Column(name = "order_id")
    public long orderId;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "contact_detail_id")
    public long contactDetailId;

    @Column(name = "address")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String address;//地址

    @Column(name = "phone_number")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String phoneNumber;

    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    @Column(name = "delivery_time")
    public long deliveryTime;//发货时间

    @Column(name = "express_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String expressNo;

    @Column(name = "express_company")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String expressCompany;

    @Column(name = "delivery_type")
    public int deliveryType;//送货类型

    @Column(name = "pickup_note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String pickupNote;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "create_time")
    public long createTime;

    @Column(name = "location")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String location = "";

    @Column(name = "schedule_time")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String scheduleTime = "";

    @Column(name = "pick_code")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String pickCode = "";

    @Column(name = "table_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String tableNo;
    @Column(name = "dine_in_numbers")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String dineInNumbers = "";
    @Column(name = "operator_id")
    public long operatorId;
    @Column(name = "need_bag")
    public boolean needBag;

    @Column(name = "operator_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String operatorName;

    @Column(name = "distance")
    public long distance;

    public static Finder<Long, OrderReceiveDetail> find = new Finder<>(OrderReceiveDetail.class);

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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPickupNote() {
        return pickupNote;
    }

    public void setPickupNote(String pickupNote) {
        this.pickupNote = pickupNote;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public long getContactDetailId() {
        return contactDetailId;
    }

    public void setContactDetailId(long contactDetailId) {
        this.contactDetailId = contactDetailId;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getDineInNumbers() {
        return dineInNumbers;
    }

    public void setDineInNumbers(String dineInNumbers) {
        this.dineInNumbers = dineInNumbers;
    }

    public boolean isNeedBag() {
        return needBag;
    }

    public void setNeedBag(boolean needBag) {
        this.needBag = needBag;
    }
}
