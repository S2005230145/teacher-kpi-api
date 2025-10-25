package actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.push.MsgActor;
import models.log.BalanceLog;
import models.msg.Msg;
import models.order.Order;
import models.order.OrderDetail;
import models.order.OrderReceiveDetail;
import models.org.Org;
import models.shop.Shop;
import models.user.Member;
import models.user.MemberBalance;
import play.libs.Json;
import utils.BalanceParam;
import utils.DateUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static controllers.push.MsgActor.SHOP_ACTOR_MAP;

/**
 * Created by win7 on 2016/7/14.
 */
public class TimerActor extends AbstractLoggingActor {

    @Inject
    DateUtils dateUtils;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorProtocol.BALANCE_LOG.class, param -> {
                    makeBalanceLog(param);
                })
                .build();
    }

    private void makeBalanceLog(ActorProtocol.BALANCE_LOG param) {
        MemberBalance balance = MemberBalance.find.query().where()
                .eq("orgId", param.balanceParam.orgId)
                .eq("uid", param.uid)
                .eq("itemId", param.itemId)
                .setMaxRows(1).findOne();
        long currentTime = dateUtils.getCurrentTimeByMills();
        if (null != balance) {
            BalanceParam balanceParam = param.balanceParam;
            BalanceLog balanceLog = new BalanceLog();
            balanceLog.setUid(param.uid);
            if (param.uid > 0) {
                Member member = Member.find.byId(param.uid);
                if (null != member) {
                    String memberNo = member.memberNo;
                    if (ValidationUtil.isEmpty(memberNo)) memberNo = member.phoneNumber;
                    balanceLog.setMemberNo(memberNo);
                    String userName = member.realName;
                    if (ValidationUtil.isEmpty(userName)) userName = member.nickName;
                    balanceLog.setMemberName(userName);
                }
            }
            balanceLog.setOrgId(balance.orgId);
            balanceLog.setShopId(balanceParam.orgId);
            balanceLog.setShopName(balanceParam.orgName);
            balanceLog.setOrderNo(balanceParam.orderNo);
            balanceLog.setItemId(param.itemId);
            balanceLog.setLeftBalance(balance.leftBalance);
            balanceLog.setFreezeBalance(balance.freezeBalance);
            balanceLog.setTotalBalance(balance.totalBalance);
            balanceLog.setChangeAmount(balanceParam.changeAmount);
            balanceLog.setBizType(balanceParam.bizType);
            balanceLog.setNote(balanceParam.desc);
            balanceLog.setOperatorId(balanceParam.operatorId);
            balanceLog.setOperatorName(balanceParam.adminName);
            balanceLog.setCreateTime(currentTime);
            balanceLog.save();

            Msg msg = new Msg();
            msg.setUid(balance.uid);
            msg.setTitle(balanceParam.desc);
            msg.setContent(balanceParam.desc);
            msg.setLinkUrl("");
            msg.setMsgType(Msg.MSG_TYPE_BALANCE);
            msg.setStatus(Msg.STATUS_NOT_READ);
            msg.setItemId(balance.itemId);
            msg.setChangeAmount(balanceParam.changeAmount);
            msg.setCreateTime(currentTime);
            msg.save();
        }
    }

}
