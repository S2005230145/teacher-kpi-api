package controllers.push;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import controllers.BaseController;
import models.admin.ShopAdmin;
import models.shop.Shop;
import play.Logger;
import play.libs.F;
import play.libs.streams.ActorFlow;
import play.mvc.Http;
import play.mvc.WebSocket;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static constants.BusinessConstant.KEY_AUTH_SEC;
import static constants.BusinessConstant.KEY_AUTH_TOKEN;


/**
 * 推送
 */
public class PushController extends BaseController {

    Logger.ALogger logger = Logger.of(PushController.class);
    private static final int TICKET_EXPIRE_TIME = 200;//10秒
    public static final String PREFIX_UID = "UID:";
    public static final String KEY_MEMBER_ID = "KEY_MEMBER_ID";

    @Inject
    ActorSystem actorSystem;

    @Inject
    Materializer materializer;

    public WebSocket handleWS() {
        return WebSocket.Json.acceptOrResult(request -> {
//            if (!sameOriginCheck(request)) return CompletableFuture.completedFuture(F.Either.Left(forbidden()));
            String uidToken = "";
            Optional<String> secOption = request.queryString(KEY_AUTH_TOKEN);
            if (secOption.isPresent()) {
                uidToken = secOption.get();
            }
            if (ValidationUtil.isEmpty(uidToken)) uidToken = getUIDFromRequest(request);
//            System.out.println("handleWS:" + uidToken);
            ShopAdmin shopAdmin;
            if (!ValidationUtil.isEmpty(uidToken)) {
                Optional<ShopAdmin> memberOptional = redis.sync().get(uidToken);
                if (!memberOptional.isPresent()) {
                    redis.remove(uidToken);
                    return CompletableFuture.completedFuture(F.Either.Left(okCustomJson(403, "token 失效")));
                }
                shopAdmin = memberOptional.get();
                if (null == shopAdmin)
                    return CompletableFuture.completedFuture(F.Either.Left(okCustomJson(403, "token 失效")));
                ShopAdmin finalShopAdmin = shopAdmin;
                return CompletableFuture.completedFuture(F.Either.Right(
                        ActorFlow.actorRef(
                                out -> MsgActor.props(out, finalShopAdmin, finalShopAdmin.id),
                                507800,
                                OverflowStrategy.dropHead(),
                                actorSystem, materializer)));
            } else {
                return CompletableFuture.completedFuture(F.Either.Left(okCustomJson(403, "token 失效")));
            }
        });
    }

    public String getUIDFromRequest(Http.RequestHeader request) {
        Optional<String> authTokenHeaderValues = request.headers().get(KEY_AUTH_TOKEN);
        if (authTokenHeaderValues.isPresent()) {
            String authToken = authTokenHeaderValues.get();
            return authToken;
        }
        return "";
    }

    private boolean sameOriginCheck(Http.RequestHeader request) {
        List<String> origins = request.getHeaders().getAll("Origin");
        if (origins.size() > 1) {
            // more than one origin found
            return false;
        }
        String origin = origins.get(0);
        return originMatches(origin);
    }

    private boolean originMatches(String origin) {
        if (origin == null) return false;
        try {
            URI url = new URI(origin);
            String host = url.getHost();
//            if (!host.equalsIgnoreCase("starnew.cn")
//                    && !host.equalsIgnoreCase("maiyatuan.cn")
//                    && !host.equalsIgnoreCase("localhost")) return false;
//            int port = url.getPort();
//            if (port < 9000 || port > 9010) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
