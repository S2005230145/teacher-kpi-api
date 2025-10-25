package models.msg;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "v1_msg")
public class Msg extends Model implements Serializable {
    
    public static final int STATUS_NOT_READ =1;
    public static final int STATUS_READ =2;
    
    
    public static final int MSG_TYPE_BALANCE =1;
    public static final int MSG_TYPE_TRANSACTIONS = 2;
    public static final int MSG_TYPE_SYSTEM =3;
    public static final int MSG_TYPE_DISPATCH =4;  //派单
    public static final int MSG_TYPE_TRANSFER =5;  //转单
    public static final int MSG_TYPE_CANCEL =6;  //退单
    public static final int MSG_TYPE_AUTO_DISPATCH =7;  //自动派单
    public static final int MSG_TYPE_SHOP_TAKE_ORDER =11;  //商家接单
    public static final int MSG_TYPE_SHOP_FINISH_MAKE =12;  //商家出餐
    public static final int MSG_TYPE_SHOP_SELF_TOOK =13;  //自提
    public static final int MSG_TYPE_SHOP_DELIVERED =14;  //商家送达

    
    private static final long serialVersionUID = -1885841224604019263L;
    
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    
    @Column(name = "uid")
    public long uid;//用户id
    
    @Column(name = "item_id")
    public int itemId;//业务ID
    
    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;
    
    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String content;
    
    @Column(name = "link_url")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String linkUrl;
    
    @Column(name = "msg_type")
    public int msgType;
    
    @Column(name = "category_id")
    public int categoryId;
    
    @Column(name = "area_id")
    public long areaId;
    
    @Column(name = "target_id")
    public long targetId;
    
    @Column(name = "status")
    public int status;
    
    @Column(name = "change_amount")
    public long changeAmount;
    
    @Column(name = "create_time")
    public long createTime;//创建时间
    
    public static Finder<Long, Msg> find = new Finder<>(Msg.class);
    
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
    
    public String getLinkUrl() {
        return linkUrl;
    }
    
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
    
    public int getMsgType() {
        return msgType;
    }
    
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public long getAreaId() {
        return areaId;
    }
    
    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }
    
    public long getTargetId() {
        return targetId;
    }
    
    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getChangeAmount() {
        return changeAmount;
    }
    
    public void setChangeAmount(long changeAmount) {
        this.changeAmount = changeAmount;
    }
}
