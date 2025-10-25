package utils;

/**
 * 订单参数辅助类
 */
public class OrderParam {

    public long uid;
    public String userName;
    public long addressId;
    public long shopId;
    public long selfTakePlaceId;
    public int deliveryType;
    public String description;
    public String shopName;
    public long totalMoney;
    public long useScore;
    public long memberCouponId;
    public long groupBuyId;
    public long realPayMoney;
    public long paidUp;
    public long couponFree;
    public long memberFee;
    public long memberFavor;
    public long mailFee;
    public int productAmount;
    public int activityType;
    public boolean joinFlashSale;
    public long dealerId;
    public String dealerCode;
    public String scheduleTime;

    public long orgId;

    public OrderParam(long uid, String userName, long addressId, long selfTakePlaceId, int deliveryType,
                      String description, long totalMoney, long useScore, long memberCouponId, long groupBuyId,
                      long realPayMoney, int productAmount, long couponFree, int activityType, long shopId, String shopName,
                      long memberFee, long memberFavor, boolean joinFlashSale, long mailFee, long dealerId, String dealerCode,
                      String scheduleTime, long paidUp, long orgId) {
        this.uid = uid;
        this.addressId = addressId;
        this.selfTakePlaceId = selfTakePlaceId;
        this.deliveryType = deliveryType;
        this.description = description;
        this.totalMoney = totalMoney;
        this.useScore = useScore;
        this.memberCouponId = memberCouponId;
        this.groupBuyId = groupBuyId;
        this.realPayMoney = realPayMoney;
        this.userName = userName;
        this.productAmount = productAmount;
        this.couponFree = couponFree;
        this.activityType = activityType;
        this.shopId = shopId;
        this.shopName = shopName;
        this.memberFee = memberFee;
        this.memberFavor = memberFavor;
        this.joinFlashSale = joinFlashSale;
        this.mailFee = mailFee;
        this.dealerId = dealerId;
        this.dealerCode = dealerCode;
        this.scheduleTime = scheduleTime;
        this.paidUp = paidUp;
        this.orgId = orgId;
    }


    public static class Builder {
        public long innerUid;
        public String innerUserName;
        public long innerAddressId;
        public long innerSelfTakePlaceId;
        public int innerDeliveryType;
        public String innerDescription;
        public String innerShopName;
        public long innerTotalMoney;
        public long innerUseScore;
        public long innerMemberCouponId;
        public long innerGroupBuyId;
        public long innerRealPayMoney;
        public int innerProductAmount;
        public long innerCouponFree;
        public int innerActivityType;
        public long innerShopId;
        public long innerMemberFee;
        public long innerMemberFavor;
        public boolean innerJoinFlashSale;
        public long innerMailFee;
        public long innerDealerId;
        public String innerDealerCode;
        public String innerScheduleTime;
        public long innerPaidUp;
        public long innerOrgId;

        public Builder() {
        }

        public Builder(long innerUid, String innerUserName, long innerAddressId, long innerSelfTakePlaceId, int innerDeliveryType, String innerDescription,
                       long innerTotalMoney, long innerUseScore, long innerMemberCouponId, long innerGroupBuyId,
                       long innerRealPayMoney, int innerProductAmount, long innerCouponFree, int innerActivityType, long innerShopId,
                       String innerShopName, long innerMemberFee, long innerMemberFavor, boolean innerJoinFlashSale, long innerMailFee,
                       long innerDealerId, String innerDealerCode, String innerScheduleTime, long innerPaidUp, long innerOrgId) {
            this.innerUid = innerUid;
            this.innerAddressId = innerAddressId;
            this.innerSelfTakePlaceId = innerSelfTakePlaceId;
            this.innerDeliveryType = innerDeliveryType;
            this.innerDescription = innerDescription;
            this.innerTotalMoney = innerTotalMoney;
            this.innerUseScore = innerUseScore;
            this.innerMemberCouponId = innerMemberCouponId;
            this.innerGroupBuyId = innerGroupBuyId;
            this.innerRealPayMoney = innerRealPayMoney;
            this.innerUserName = innerUserName;
            this.innerProductAmount = innerProductAmount;
            this.innerCouponFree = innerCouponFree;
            this.innerActivityType = innerActivityType;
            this.innerShopId = innerShopId;
            this.innerShopName = innerShopName;
            this.innerMemberFee = innerMemberFee;
            this.innerMemberFavor = innerMemberFavor;
            this.innerJoinFlashSale = innerJoinFlashSale;
            this.innerMailFee = innerMailFee;
            this.innerDealerId = innerDealerId;
            this.innerDealerCode = innerDealerCode;
            this.innerScheduleTime = innerScheduleTime;
            this.innerPaidUp = innerPaidUp;
            this.innerOrgId = innerOrgId;
        }

        public Builder uid(long uid) {
            this.innerUid = uid;
            return this;
        }

        public Builder addressId(long addressId) {
            this.innerAddressId = addressId;
            return this;
        }

        public Builder selfTakePlaceId(long selfTakePlaceId) {
            this.innerSelfTakePlaceId = selfTakePlaceId;
            return this;
        }

        public Builder deliveryType(int deliveryType) {
            this.innerDeliveryType = deliveryType;
            return this;
        }

        public Builder description(String description) {
            this.innerDescription = description;
            return this;
        }

        public Builder totalMoney(long totalMoney) {
            this.innerTotalMoney = totalMoney;
            return this;
        }

        public Builder useScore(long useScore) {
            this.innerUseScore = useScore;
            return this;
        }

        public Builder memberCouponId(long memberCouponId) {
            this.innerMemberCouponId = memberCouponId;
            return this;
        }

        public Builder groupBuyId(long groupBuyId) {
            this.innerGroupBuyId = groupBuyId;
            return this;
        }

        public Builder realPayMoney(long realPayMoney) {
            this.innerRealPayMoney = realPayMoney;
            return this;
        }

        public Builder userName(String userName) {
            this.innerUserName = userName;
            return this;
        }

        public Builder productAmount(int productAmount) {
            this.innerProductAmount = productAmount;
            return this;
        }

        public Builder couponFree(long couponFree) {
            this.innerCouponFree = couponFree;
            return this;
        }

        public Builder activityType(int activityType) {
            this.innerActivityType = activityType;
            return this;
        }

        public Builder shopId(long shopId) {
            this.innerShopId = shopId;
            return this;
        }

        public Builder orgId(long orgId) {
            this.innerOrgId = orgId;
            return this;
        }

        public Builder shopName(String shopName) {
            this.innerShopName = shopName;
            return this;
        }

        public Builder memberFee(long memberFee) {
            this.innerMemberFee = memberFee;
            return this;
        }

        public Builder memberFavor(long memberFavor) {
            this.innerMemberFavor = memberFavor;
            return this;
        }

        public Builder joinFlashSale(boolean joinFlashSale) {
            this.innerJoinFlashSale = joinFlashSale;
            return this;
        }

        public Builder mailFee(long mailFee) {
            this.innerMailFee = mailFee;
            return this;
        }

        public Builder dealerId(long dealerId) {
            this.innerDealerId = dealerId;
            return this;
        }

        public Builder dealerCode(String dealerCode) {
            this.innerDealerCode = dealerCode;
            return this;
        }

        public Builder scheduleTime(String scheduleTime) {
            this.innerScheduleTime = scheduleTime;
            return this;
        }

        public Builder paidUp(long paidUp) {
            this.innerPaidUp = paidUp;
            return this;
        }

        public OrderParam build() {
            return new OrderParam(innerUid, innerUserName, innerAddressId, innerSelfTakePlaceId, innerDeliveryType, innerDescription,
                    innerTotalMoney, innerUseScore, innerMemberCouponId, innerGroupBuyId, innerRealPayMoney,
                    innerProductAmount, innerCouponFree, innerActivityType, innerShopId, innerShopName,
                    innerMemberFee, innerMemberFavor, innerJoinFlashSale, innerMailFee, innerDealerId, innerDealerCode,
                    innerScheduleTime, innerPaidUp, innerOrgId);
        }
    }

}
