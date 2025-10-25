package utils;

import actor.ActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.ebean.DB;
import io.ebean.SqlUpdate;
import models.order.Order;
import models.user.MemberBalance;
import play.Logger;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.concurrent.TimeUnit;

import static constants.BusinessConstant.TRANSACTION_TYPE_SUBTRACT_SCORE_GAVE_FOR_CANCEL_ORDER;

/**
 * cache utils
 */
@Singleton
public class BalanceUtils {
    Logger.ALogger logger = Logger.of(BalanceUtils.class);

    @Inject
    DateUtils dateUtils;
    @Inject
    @Named("timerActor")
    ActorRef timerActorRef;
    private final ActorSystem system;

    @Inject
    public BalanceUtils(ActorSystem system) {
        this.system = system;
    }

    /**
     * 修改用户币种金额表,统一用加的方式,如果需要扣除,则需要对应的参数设为负数
     *
     * @param param
     * @return
     */
    public MemberBalance saveChangeBalance(BalanceParam param, boolean needLog) {
        if (null == param) return null;
        if (ValidationUtil.isEmpty(param.desc)) param.desc = "";
        long currentTime = dateUtils.getCurrentTimeByMills();

        MemberBalance balance = MemberBalance.find.query().where()
                .eq("orgId", param.orgId)
                .eq("uid", param.memberId)
                .eq("itemId", param.itemId)
                .setMaxRows(1).forUpdate().findOne();
        String resultDesc = param.desc;
        if (null == balance) {
            balance = new MemberBalance();
            balance.setOrgId(param.orgId);
            balance.setUid(param.memberId);
            balance.setItemId(param.itemId);
            balance.setLeftBalance(param.leftBalance);
            balance.setFreezeBalance(param.freezeBalance);
            balance.setTotalBalance(param.totalBalance);
            balance.setUpdateTime(currentTime);
            balance.setCreatedTime(currentTime);
            balance.save();
        } else {
            String sql = "UPDATE " +
                    "    `v1_member_balance` AS `dest`, " +
                    "    ( SELECT * FROM `v1_member_balance` " +
                    "        WHERE org_id = :orgId AND uid = :uid   AND item_id = :itemId limit 1" +
                    "    ) AS `src`" +
                    "   SET" +
                    "       `dest`.`left_balance` = `src`.`left_balance`+ (:leftBalance)," +
                    "       `dest`.`freeze_balance` = `src`.`freeze_balance`+ (:freezeBalance)," +
                    "       `dest`.`total_balance` = `src`.`total_balance`+ (:totalBalance), " +
                    "       `dest`.`update_time` =   :updateTime   " +
                    "   WHERE" +
                    "   dest.uid = :uid " +
                    "   AND dest.item_id = :itemId " +
                    ";";
            SqlUpdate sqlUpdate = DB.sqlUpdate(sql);
            sqlUpdate.setParameter("orgId", param.orgId);
            sqlUpdate.setParameter("uid", param.memberId);
            sqlUpdate.setParameter("itemId", param.itemId);
            sqlUpdate.setParameter("leftBalance", param.leftBalance);
            sqlUpdate.setParameter("freezeBalance", param.freezeBalance);
            sqlUpdate.setParameter("totalBalance", param.totalBalance);
            sqlUpdate.setParameter("updateTime", currentTime);
            sqlUpdate.executeNow();
        }
        if (needLog) insertBalanceLog(balance, param);
        return balance;
    }

    public void insertBalanceLog(MemberBalance balanceParam, BalanceParam param) {
        system.scheduler().scheduleOnce(
                Duration.create(500, TimeUnit.MILLISECONDS),
                timerActorRef,
                new ActorProtocol.BALANCE_LOG(balanceParam.uid, balanceParam.itemId, param),
                system.dispatcher(),
                ActorRef.noSender()
        );
    }

}
