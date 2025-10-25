package service;

import models.admin.GroupAction;
import models.user.MemberLevel;
import models.user.MemberScoreConfig;
import play.Logger;
import play.cache.NamedCache;
import utils.CacheUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static constants.RedisKeyConstant.ADMIN_GROUP_ACTION_KEY_SET;

/**
 * App初始化应用，一般用于初始化缓存等初始化动作
 */
@Singleton
public class AppInit {
    Logger.ALogger logger = Logger.of(AppInit.class);
    
    @Inject
    CacheUtils cacheUtils;
    
    
    @Inject
    @NamedCache("redis")
    protected play.cache.redis.AsyncCacheApi redis;
    
    /**
     * 读取常用配置到缓存当中
     */
    
    public void saveToCache() {
        try {
            loadParamConfig();
            loadCouponConfig();
            saveAdminCache();
        } catch (Exception e) {
            logger.error("saveToCache:" + e.getMessage());
        }
        
    }
    
    

    
    //加载参数配置
    private void loadParamConfig() {
        cacheUtils.updateParamConfigCache();
    }
    
    //加载优惠券配置
    private void loadCouponConfig() {
        logger.info("加载优惠券缓存");
        cacheUtils.updateCouponConfigCache();
    }
    
    public void saveAdminCache() {
        logger.info("保存权限信息到缓存");
        redis.remove(ADMIN_GROUP_ACTION_KEY_SET);
        List<GroupAction> groupActions = GroupAction.find.all();
        Map<Integer, Set<String>> map = new HashMap<>();
        groupActions.forEach((groupAction -> {
            Set<String> set = map.get(groupAction.groupId);
            if (null == set) set = new HashSet<>();
            set.add(groupAction.actionId);
            map.put(groupAction.groupId, set);
        }));
        map.forEach((groupId, actionSet) -> {
            String key = cacheUtils.getGroupActionKey(groupId);
            redis.set(key, actionSet);
        });
        redis.set(ADMIN_GROUP_ACTION_KEY_SET, ADMIN_GROUP_ACTION_KEY_SET);
    }
    
}
