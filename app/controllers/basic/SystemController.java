package controllers.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.BusinessConstant;
import controllers.BaseController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.system.ParamConfig;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import utils.ValidationUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SystemController extends BaseController {

    Logger.ALogger logger = Logger.of(SystemController.class);

    /**
     * @api {GET} /v2/shop/param_config/?page=&key= 01获取配置列表
     * @apiName listParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200) {int} pages 页数
     * @apiSuccess (Success 200) {JsonArray} list 列表
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} key key
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 200){String} note 中文备注
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    public CompletionStage<Result> listParamConfig(Http.Request request, String key) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503();
            ExpressionList<ParamConfig> expressionList = ParamConfig.find.query().where()
                    .eq("orgId", admin.orgId)
                    .eq("enable", true);
            if (!ValidationUtil.isEmpty(key)) expressionList.icontains("key", key);
            List<ParamConfig> list = expressionList.orderBy().desc("id").orderBy().asc("key")
                    .findList();
            ObjectNode result = Json.newObject();
            result.put(CODE, CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }

    /**
     * @api {GET} /v2/shop/param_config/param_config/:configId/ 02获取配置详情
     * @apiName getParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200) {int} code 200 请求成功
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} key key
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 200){String} note 中文备注
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    public CompletionStage<Result> getParamConfig(Http.Request request, long configId) {
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503();
            if (configId < 1) return okCustomJson(CODE40001, "参数错误");
            ParamConfig config = ParamConfig.find.query().where()
                    .eq("id", configId)
                    .eq("orgId", admin.orgId)
                    .eq("enable", true)
                    .setMaxRows(1)
                    .findOne();
            if (null == config) return okCustomJson(CODE40002, "找不到该配置");
            ObjectNode result = (ObjectNode) Json.toJson(config);
            result.put(CODE, CODE200);
            return ok(result);
        });
    }


    private void updateParamConfigCache() {
        cacheUtils.updateParamConfigCache();
    }

    /**
     * @api {POST} /v2/shop/param_config/:id/ 03更新配置value值
     * @apiName getParamConfig
     * @apiGroup SHOP-PARAM-CONFIG
     * @apiSuccess (Success 200){int} id 配置id
     * @apiSuccess (Success 200){String} value 值
     * @apiSuccess (Success 40001) {int} code 40001 参数错误
     * @apiSuccess (Success 40002) {int} code 40002 配置不存在
     * @apiSuccess (Success 40003) {int} code 40003 该配置的KEY已存在
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateParamConfig(Http.Request request, long configId) {
        JsonNode jsonNode = request.body().asJson();
        return businessUtils.getUserIdByAuthToken(request).thenApplyAsync((admin) -> {
            if (null == admin || admin.orgId < 1) return unauth503();
            if (null == jsonNode) return okCustomJson(CODE40001, "参数错误");
            String value = jsonNode.findPath("value").asText();

            ParamConfig paramConfig = ParamConfig.find.byId(configId);
            if (null == paramConfig || paramConfig.orgId != admin.orgId) return okCustomJson(CODE40001, "参数不存在");
            paramConfig.setValue(value);
            paramConfig.save();
            updateParamConfigCache();
            return okJSON200();
        });

    }
}
