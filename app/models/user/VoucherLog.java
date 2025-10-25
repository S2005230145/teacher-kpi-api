package models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;

@Entity
@Table(name = "v1_voucher_log")
public class VoucherLog extends Model {
    public static final String BALANCE_DIRECTION_INCOME = "收入";
    public static final String BALANCE_DIRECTION_OUTCOME = "支出";

    public static final int STATUS_NORMAL = 1;//processing
    public static final int STATUS_TO_TRACE = 5;//processing
    public static final int STATUS_FINISH = 20;//finish
    public static final int STATUS_DISCARD = -1;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "category_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String categoryName = "";

    @Column(name = "category_id")
    public long categoryId;

    @Column(name = "balance_type")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String balanceType = "";

    @Column(name = "voucher_no")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String voucherNo = "";

    @Column(name = "digest")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String digest = "";

    @Column(name = "balance_direction")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String balanceDirection;

    @Column(name = "amount")
    public long amount;

    @Column(name = "uid")
    public long uid;

    @Column(name = "status")
    public int status;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "is_public")
    public boolean isPublic;

    @Column(name = "user_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String userName = "";

    @Column(name = "participants")
    public long participants;
    @Column(name = "participants_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String participantsName = "";

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter = "";

    @Column(name = "balance_time")
    public long balanceTime;

    @Column(name = "date")
    public long date;

    @Column(name = "create_time")
    public long createTime;

    public static Finder<Long, VoucherLog> find = new Finder<>(VoucherLog.class);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getBalanceDirection() {
        return balanceDirection;
    }

    public void setBalanceDirection(String balanceDirection) {
        this.balanceDirection = balanceDirection;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public long getBalanceTime() {
        return balanceTime;
    }

    public void setBalanceTime(long balanceTime) {
        this.balanceTime = balanceTime;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getParticipants() {
        return participants;
    }

    public void setParticipants(long participants) {
        this.participants = participants;
    }

    public String getParticipantsName() {
        return participantsName;
    }

    public void setParticipantsName(String participantsName) {
        this.participantsName = participantsName;
    }
}
