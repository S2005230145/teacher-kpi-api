package controllers.push;

import actor.ManageProtocol;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.admin.ShopAdmin;
import play.libs.Json;
import utils.ValidationUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static constants.BusinessConstant.PING;
import static constants.BusinessConstant.PONG;

/**
 * 推送ACTOR
 */
public class MsgActor extends AbstractLoggingActor {
    public static final Map<Long, ActorRef> SHOP_ACTOR_MAP = new WeakHashMap<>();
    private static final Set<ActorRef> set = new HashSet<>();

    public static Props props(ActorRef out, ShopAdmin adminMember, long adminId) {
        return Props.create(MsgActor.class, out, adminMember, adminId);
    }

    public static final String WS_TYPE_HELLO = "hello";
    public static final String WS_TYPE_PING = "ping";
    public static final String WS_TYPE_REQ = "req";
    public static final String WS_TYPE_SUB = "sub";
    public static final String WS_TYPE_UNSUB = "unsub";

    private final ShopAdmin adminMember;
    private final long adminId;
    ActorRef out;

    public MsgActor(ActorRef out, ShopAdmin adminMember, long adminId) {
        this.adminMember = adminMember;
        this.adminId = adminId;
        if (null != out) {
            this.out = out;
            if (null != adminMember && adminMember.id > 0) SHOP_ACTOR_MAP.put(adminMember.id, out);
//            set.add(out);
            ObjectNode node = Json.newObject();
            node.put("type", WS_TYPE_HELLO);
            node.put("ts", System.currentTimeMillis());
            out.tell(node, self());
        }
    }

    @Override
    public void postStop() throws Exception {
        if (null != adminMember) SHOP_ACTOR_MAP.remove(adminMember.id);
        if (null != out) set.remove(out);
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                //推送事件
                .match(ManageProtocol.CHANNEL_MSGS.class, msg -> {
                    log().info("MsgActor:" + msg.content);
                })
                .match(ObjectNode.class, param -> {
                    String type = param.findPath("type").asText();
                    if (!ValidationUtil.isEmpty(type)) {
                        if (type.equalsIgnoreCase(PING)) {
                            ObjectNode node = Json.newObject();
                            node.put("type", PONG);
                            node.put("ts", System.currentTimeMillis());
                            if (null != out) {
                                out.tell(node, ActorRef.noSender());
                                SHOP_ACTOR_MAP.put(adminId, out);
                            }
                        } else {
                            log().info(Json.stringify(param));
                            long uid = param.findPath("uid").asLong();
                            param.put("type", "TYPE_VCODE_SENT");
                            ManageProtocol.CHANNEL_MSGS msg = new ManageProtocol.CHANNEL_MSGS(Json.stringify(param));
                            self().tell(msg, ActorRef.noSender());
                        }
                    }
                })
                .build();
    }


    @Override
    public void unhandled(Object message) {
        super.unhandled(message);
    }

}
