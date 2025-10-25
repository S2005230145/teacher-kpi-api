package actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.org.Org;
import play.cache.NamedCache;
import play.libs.Json;
import play.libs.ws.WSClient;
import utils.ConfigUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static constants.RedisKeyConstant.STABLE_ACCESS_TOKEN;
import static utils.wechatpay.WechatConfig.STABLE_ACCESS_TOKEN_API_URL;

/**
 * Created by win7 on 2016/7/14.
 */
public class TokenActor extends AbstractLoggingActor {
    
    @Inject
    WSClient wsClient;
    
    @Inject
    @NamedCache("redis")
    protected play.cache.AsyncCacheApi redis;
    
    @Inject
    EncodeUtils encodeUtils;
    
    @Inject
    ConfigUtils configUtils;
    
    public static Props getProps() {
        return Props.create(TokenActor.class);
    }
    
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorProtocol.STABLE_ACCESS_TOKEN.class, str -> {
                    refreshAccessToken();
                })
                .match(ActorProtocol.LISTEN_STABLE_ACCESS_TOKEN.class, str -> {
                    checkStableToken();
                })
                .build();
    }
    
    private void refreshAccessToken() {
        String miniAppId = configUtils.getWechatMiniAppId().trim();
        String secretCode = configUtils.getWechatMiniAppSecretCode().trim();
        if (!ValidationUtil.isEmpty(miniAppId) && !ValidationUtil.isEmpty(secretCode)) {
            String url = STABLE_ACCESS_TOKEN_API_URL;
            ObjectNode param = Json.newObject();
            param.put("grant_type", "client_credential");
            param.put("appid", miniAppId);
            param.put("secret", secretCode);
            wsClient.url(url).post(param).thenAcceptAsync(response -> {
                JsonNode node = response.asJson();
                if (null != node) {
                    String token = node.findPath("access_token").asText();
                    int expiresIn = node.findPath("expires_in").asInt();
                    if (!ValidationUtil.isEmpty(token)) {
                        //更新缓存
                        log().info("refresh stable access token:" + token);
                        redis.set(STABLE_ACCESS_TOKEN, token, expiresIn - 5);
                    }
                }
            });
        }
        
    }
    
    private void checkStableToken() {
        Optional<String> optional = redis.sync().get(STABLE_ACCESS_TOKEN);
        String token = "";
        if (optional.isPresent()) token = optional.get();
        if (ValidationUtil.isEmpty(token)) {
            refreshAccessToken();
        }
        
    }
}
