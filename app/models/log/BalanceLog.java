package models.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_balance_log")
public class BalanceLog extends Model implements Serializable {
    private static final long serialVersionUID = -1885841224604019263L;

    public static final int FREEZE_STATUS_NOT_SET = 0;
    public static final int FREEZE_STATUS_TO_HANDLE = 1;
    public static final int FREEZE_STATUS_HANDLED = 2;
    public static final int FREEZE_STATUS_CANCELED = 3;
    public static final int BALANCE_TYPE_USER = 10;
    public static final int BALANCE_TYPE_SHOP = 20;
    public static final int BALANCE_TYPE_DRIVER = 30;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "order_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String orderNo;

    @Column(name = "uid")
    public long uid;//用户id

    @Column(name = "member_no")
    public String memberNo;

    @Column(name = "member_name")
    public String memberName;

    @Column(name = "item_id")
    public int itemId;


    @Column(name = "balance_type")
    public int balanceType;

    @Column(name = "left_balance")
    public long leftBalance;

    @Column(name = "freeze_balance")
    public long freezeBalance;

    @Column(name = "total_balance")
    public long totalBalance;

    @Column(name = "change_amount")
    public long changeAmount;

    @Column(name = "biz_type")
    public int bizType;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "freeze_status")
    public int freezeStatus;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "shop_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopName;

    @Column(name = "operator_id")
    public long operatorId;

    @Column(name = "operator_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String operatorName;
    @Column(name = "staff_id")
    public long staffId;

    @Column(name = "staff_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String staffName;

    @Column(name = "create_time")
    public long createTime;//创建时间

    @Transient
    public String changeAmountStr;

    @Transient
    public String freezeBalanceStr;

    public static Finder<Long, BalanceLog> find = new Finder<>(BalanceLog.class);

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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getLeftBalance() {
        return leftBalance;
    }

    public void setLeftBalance(long leftBalance) {
        this.leftBalance = leftBalance;
    }

    public long getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(long freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getChangeAmountStr() {
        return changeAmountStr;
    }

    public void setChangeAmountStr(String changeAmountStr) {
        this.changeAmountStr = changeAmountStr;
    }

    public String getFreezeBalanceStr() {
        return freezeBalanceStr;
    }

    public void setFreezeBalanceStr(String freezeBalanceStr) {
        this.freezeBalanceStr = freezeBalanceStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(int freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(int balanceType) {
        this.balanceType = balanceType;
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

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
