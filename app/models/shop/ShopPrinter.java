package models.shop;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.Entity;
import myannotation.EscapeHtmlSerializer;

import jakarta.persistence.*;

@Entity
@Table(name = "v1_shop_printer")
public class ShopPrinter extends Model {
    public static final int TYPE_ONLINE_TICKET = 1;
    public static final int TYPE_OFFLINE_TICKET = 2;
    public static final int TYPE_ONLINE_LABEL = 3;
    public static final int TYPE_OFFLINE_LABEL = 4;
    public static final int TYPE_PURCHASING_TICKET = 5;
    public static final int TYPE_COOK_TICKET = 6;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "org_id")
    public long orgId;

    @Column(name = "shop_id")
    public long shopId;

    @Column(name = "printer_type")
    public int printerType;

    @Column(name = "shop_name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String shopName;

    @Column(name = "printer_sn")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String printerSn;

    @Column(name = "printer_key")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String printerKey;

    @Column(name = "note")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String note;

    @Column(name = "filter")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filter;

    @Column(name = "enable")
    public boolean enable;

    @Column(name = "update_time")
    public long updateTime;

    @Column(name = "sort")
    public int sort;

    @Column(name = "only_products")
    public boolean onlyProducts;

    @Column(name = "category_list")
    public String categoryList;


    public static Finder<Long, ShopPrinter> find = new Finder<>(ShopPrinter.class);

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPrinterSn() {
        return printerSn;
    }

    public void setPrinterSn(String printerSn) {
        this.printerSn = printerSn;
    }

    public String getPrinterKey() {
        return printerKey;
    }

    public void setPrinterKey(String printerKey) {
        this.printerKey = printerKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPrinterType() {
        return printerType;
    }

    public void setPrinterType(int printerType) {
        this.printerType = printerType;
    }

    public boolean isOnlyProducts() {
        return onlyProducts;
    }

    public void setOnlyProducts(boolean onlyProducts) {
        this.onlyProducts = onlyProducts;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(String categoryList) {
        this.categoryList = categoryList;
    }
}
