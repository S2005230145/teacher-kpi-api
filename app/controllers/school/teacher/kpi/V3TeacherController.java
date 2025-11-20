package controllers.school.teacher.kpi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseAdminSecurityController;

import io.ebean.*;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.school.kpi.v3.*;
import models.table.ParseResult;
import models.user.Role;
import models.user.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repository.V3TeacherRepository;
import service.FileParseService;
import utils.AssessmentPDF;
import utils.Pair;
import utils.ValidationUtil;
import utils.parse.WordParser;
import utils.parse.WordTableTitleExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;


public class V3TeacherController extends BaseAdminSecurityController {
    @Inject
    V3TeacherRepository v3TeacherRepository;

    @Inject
    FileParseService fileParseService;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final Boolean is_DEV=false;

    //极大值
    private static final Double mxValue=5000.0;

    /**
     * @api {POST} /v1/tk/kpi/add/  01 kpi添加
     * @apiName addKpi
     * @apiGroup Teacher
     *
     * @apiDescription 批量创建kpi
     *
     * @apiParam {String} title KPI标题
     *
     * @apiParamExample {json} 请求示例:
     *     [
     *       {
     *         "title": ""//kpi标题名称
     *       }
     *     ]
     *
     * @apiSuccess (Success 200) {int} code 200
     * @apiSuccess (Success 200) {String[]} reason []
     *
     * @apiSuccess (Error 500) {int} code 500
     * @apiSuccess (Error 500) {String[]} reason 错误信息
     */
    public CompletionStage<Result> addKpi(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<KPI> kpiList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.addKpi(kpiList);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    /**
     * @api {POST} /v1/tk/regular/add/  02 kpi指标及其对应内容添加
     * @apiName add
     * @apiGroup Teacher
     *
     * @apiDescription 批量创建指标及相关要素
     *
     * @apiParam {String} indicatorName 指标名称
     * @apiParam {String} subName 评价子名称
     * @apiParam {Long} kpiId 对应的KPI的ID
     *
     * @apiParam {String} element 要素名称 当为null时会切割content的@#$作为合并单元
     * @apiParam {String} criteria 评价标准 合格，不合格(暂空)
     * @apiParam {String} content 内容名称 多个内容需要用中文的、分开，当出现@#$时会切割它，否则切割、
     *
     * @apiParamExample {json} 请求示例:
     *     [
     *       {
     *         "indicatorName":"",//指标名称
     *         "subName":"",//指标附属名称
     *         "kpiId":1,//kpi对应Id
     *         "elementList":[
     *             {
     *                 "element":null,//要素名称
     *                 "criteria":"",//标准
     *                 "contentList":[
     *                     {
     *                         "content":"12、123、12314"//内容，多个内容需要用中文的、分开，当出现@#$时会切割它，否则切割、
     *                     }
     *                 ]
     *             }
     *          ]
     *       }
     *     ]
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} reason 错误信息
     */
    public CompletionStage<Result> add(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Indicator> indicatorList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.addAll(indicatorList);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    /**
     * @api {POST} /v1/tk/singleKpi/add/  03 添加单个kpi指标及其对应内容添加
     * @apiName addSingleKpi
     * @apiGroup Teacher
     *
     * @apiDescription 添加KPI和其相关信息
     *
     * @apiParamExample {json} 请求示例:
     * {
     *      "title":"福清市XXX",//KPI标题
     *      "indicatorList":[//指标列表
     *          {
     *              "indicatorName":"师德师范",//指标名称
     *              "subName":"（优、合格、不合格）",//指标附属信息
     *              "elementList":[
     *                  {
     *                      "element":null,//评价要素名称
     *                      "criteria":"是否合格",//评价标准
     *                      "type":0//评分方式 0:手动计算,1:自动计算,2:提交上报(不填默认手动)
     *                      "contentList":[
     *                          {
     *                              "content":"12333@#$231414214@#$asda";//优先对 @#$ 切割，没有的话对、切割
     *                          }
     *                      ]
     *                  }
     *
     *              ]
     *          }
     *      ]
     * }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} reason 错误信息
     */
    public CompletionStage<Result> addSingleKpi(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            KPI kpi = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.addKpiSingle(kpi);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    /**
     * @api {POST} /v1/tk/getList/  04 获取所有指标
     * @apiName getList
     * @apiGroup Teacher
     *
     * @apiDescription 获取所有指标及相关下的信息,也可以获取用户的所有指标
     *
     * @apiParam {int} currentPage 当前页面
     * @apiParam {int} pageSize 显示条数
     * @apiParam {Long} userId (可选)用户ID
     *
     * @apiParamExample {json} 请求示例:
     *       {
     *         "currentPage":1,
     *         "pageSize":10
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccessExample {json} data 数据详情
     * {
     *     "data": {
     *         "list": [//所有指标信息,无KPI
     *             {
     *                 "id": 1,
     *                 "kpiId": 1,
     *                 "indicatorName": "师德师风",
     *                 "subName": "（优、合格、不合格）",
     *                 "elementList": [
     *                     {
     *                         "id": 1,
     *                         "indicatorId": 1,
     *                         "element": null,
     *                         "contentList": [
     *                             {
     *                                 "id": 1,
     *                                 "elementId": 1,
     *                                 "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",
     *                                 "score":6.0,
     *                             },
     *                             {
     *                                 "id": 2,
     *                                 "elementId": 1,
     *                                 "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
     *                                 "score":6.0,
     *                             },
     *                             {
     *                                 "id": 3,
     *                                 "elementId": 1,
     *                                 "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
     *                                 "score":6.0,
     *                             }
     *                         ],
     *                         "criteria": "是否合格，不设分值",
     *                         "type": 0
     *                     }
     *                 ]
     *             },
     *             {
     *                 "id": 2,
     *                 "kpiId": 1,
     *                 "indicatorName": "教育教学常规",
     *                 "subName": "30-40 分",
     *                 "elementList": [
     *                     {
     *                         "id": 2,
     *                         "indicatorId": 2,
     *                         "element": "1.出勤情况",
     *                         "contentList": [
     *                             {
     *                                 "id": 4,
     *                                 "elementId": 2,
     *                                 "content": "病假"
     *                             },
     *                             {
     *                                 "id": 5,
     *                                 "elementId": 2,
     *                                 "content": "事假"
     *                             },
     *                             {
     *                                 "id": 6,
     *                                 "elementId": 2,
     *                                 "content": "迟到"
     *                             },
     *                             {
     *                                 "id": 7,
     *                                 "elementId": 2,
     *                                 "content": "旷课"
     *                             },
     *                             {
     *                                 "id": 8,
     *                                 "elementId": 2,
     *                                 "content": "政治学习"
     *                             },
     *                             {
     *                                 "id": 9,
     *                                 "elementId": 2,
     *                                 "content": "教研活动"
     *                             },
     *                             {
     *                                 "id": 10,
     *                                 "elementId": 2,
     *                                 "content": "学校会议及其他集体活动出勤情况"
     *                             }
     *                         ],
     *                         "criteria": null,
     *                         "type": 0
     *                     }
     *                 ]
     *             }
     *         ],
     *         "totalCount": 5,
     *         "totalPageCount": 1,
     *         "pageIndex": 0,
     *         "futureCount": {
     *             "cancelled": false,
     *             "done": true
     *         },
     *         "pageSize": 10
     *     }
     * }
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){Object[]} data null
     * @apiSuccess (Error 500){String[]} reason 错误信息
     */
    public CompletionStage<Result> getList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            //条件
            long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?0:jsonNode.findPath("kpiId").asLong());
            long id=(jsonNode.findPath("id") instanceof MissingNode ?0:jsonNode.findPath("id").asLong());
            String indicatorName=(jsonNode.findPath("indicatorName") instanceof MissingNode ?null:jsonNode.findPath("indicatorName").asText());
            String subName=(jsonNode.findPath("subName") instanceof MissingNode ?null:jsonNode.findPath("subName").asText());

            ExpressionList<Indicator> expressionList = Indicator.find.query().where();
            if(userId!=null){
                Set<Long> kpiIds = TeacherKPIScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherKPIScore::getKpiId).collect(Collectors.toSet());
                expressionList.in("kpi_id",kpiIds);
            }
            if(kpiId>0) expressionList.eq("kpi_id",kpiId);
            if(id>0) expressionList.eq("id",id);
            if(indicatorName!=null&&!indicatorName.isEmpty()) expressionList.icontains("indicator_name",indicatorName);
            if(subName!=null&&!subName.isEmpty()) expressionList.icontains("sub_name",subName);

            Pair<PagedList<Indicator>, List<String>> list = v3TeacherRepository.getList(expressionList,currentPage,pageSize);
            ObjectNode node = Json.newObject();
            node.put("code", CODE200);
            node.set("list",Json.toJson(list.first().getList()));
            node.put("pages", list.first().getTotalPageCount());
            return ok(node);
        });
    }

    /**
     * @api {POST} /v1/tk/getKpiList/  05 获取所有KPI
     * @apiName getKpiList
     * @apiGroup Teacher
     *
     * @apiDescription 获取所有KPI以及下方的所有信息，也可获得对应用户的KPI
     *
     * @apiParam {int} currentPage 当前页面
     * @apiParam {int} pageSize 显示条数
     * @apiParam {Long} userId (可选)用户ID
     *
     * @apiParamExample {json} 请求示例:
     *       {
     *         "currentPage":1,
     *         "pageSize":10,
     *         "userId"1//会查询该用户kpi
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccessExample {json} 请求示例:
     * {
     *     "code": 200,
     *     "data": {
     *         "list": [
     *             {
     *                 "id": 1,
     *                 "title": "福清市中小学、中职学校教师绩效考核要点",//KPI名称(标题)
     *                 "indicatorList": [
     *                     {
     *                         "id": 1,
     *                         "kpiId": 1,//kpiId
     *                         "indicatorName": "师德师风",//指标名称
     *                         "subName": "（优、合格、不合格）",//指标附属名称
     *                         "elementList": [//要素内容
     *                             {
     *                                 "id": 1,
     *                                 "indicatorId": 1,
     *                                 "element": null,
     *                                 "contentList": [
     *                                     {
     *                                         "id": 1,
     *                                         "elementId": 1,
     *                                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",
     *                                         "score":6.0,
     *                                     },
     *                                     {
     *                                         "id": 2,
     *                                         "elementId": 1,
     *                                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
     *                                         "score":6.0,
     *                                     },
     *                                     {
     *                                         "id": 3,
     *                                         "elementId": 1,
     *                                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
     *                                         "score":6.0,
     *                                     }
     *                                 ],
     *                                 "criteria": "是否合格，不设分值",
     *                                 "type": 0
     *                             }
     *                         ]
     *                        }
     *                     }
     *                 ]
     *             }
     *         ],
     *         "pageIndex": 0,
     *         "futureCount": {
     *             "cancelled": false,
     *             "done": true
     *         },
     *         "pageSize": 10,
     *         "totalPageCount": 1,
     *         "totalCount": 1
     *     },
     *     "reason": []
     * }
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){Object[]} data null
     * @apiSuccess (Error 500){String[]} reason 错误信息
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> getKpiList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            //条件
            Long id=(jsonNode.findPath("id") instanceof MissingNode || jsonNode.findPath("id").isEmpty() ?null:jsonNode.findPath("id").asLong());
            String title=(jsonNode.findPath("title") instanceof MissingNode ?null:jsonNode.findPath("title").asText());

            ExpressionList<KPI> expressionList = KPI.find.query().where();
            if(userId!=null){
                Set<Long> kpiIds = TeacherKPIScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherKPIScore::getKpiId).collect(Collectors.toSet());
                expressionList.in("id",kpiIds);
            }
            if(id!=null) expressionList.eq("id",id);
            if(title!=null&&!title.isEmpty()) expressionList.icontains("title",title);

            PagedList<KPI> pagedList = expressionList.setFirstRow(0).setMaxRows(5).findPagedList();

            List<KPI> kpiList=pagedList.getList();
            int totalPageCount = pagedList.getTotalPageCount();
            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.set("list",Json.toJson(kpiList));
            node.put("pages", totalPageCount);
            return ok(node);
        });
    }

    //测试
    public CompletionStage<Result> getKpiListGet(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            ExpressionList<KPI> expressionList = KPI.find.query().where();

            List<KPI> list = expressionList.findList();

            ObjectNode node = Json.newObject();
            node.put("code", CODE200);
            node.set("list",Json.toJson(list));
            return ok(node);
        });
    }

    /**
             * @api {POST} /v1/tk/getElementList/  06 获取所有Element
             * @apiName getElementList
             * @apiGroup Teacher
             *
             * @apiDescription 获取所有Element以及下方的所有信息，也可获得对应用户的要素
             *
             * @apiParam {int} currentPage 当前页面
             * @apiParam {int} pageSize 显示条数
             * @apiParam {Long} userId (可选)用户ID
             *
             * @apiParamExample {json} 请求示例:
             *       {
             *         "currentPage":1,
             *         "pageSize":10,
             *         "userId":1//会查询该用户的评价要素
             *       }
             *
             * @apiSuccess (Success 200){int} code 200
             * @apiSuccess (Success 200){Object[]} data 数据
             * @apiSuccessExample {json} 请求示例:
             * "data": {
             *         "list": [
             *             {
             *                 "id": 1,
             *                 "title": "福清市中小学、中职学校教师绩效考核要点",//Kpi名称
             *                 "indicatorList": [
             *                     {
             *                         "id": 1,
             *                         "kpiId": 1,//KpiId
             *                         "indicatorName": "师德师风",//指标名称
             *                         "subName": "（优、合格、不合格）",//指标附属名称
             *                         "elementList": [
             *                             {
             *                                 "id": 1,
             *                                 "indicatorId": 1,//指标ID
             *                                 "element": null,//要素名称
             *                                 "contentList": [//内容区
             *                                     {
             *                                         "id": 1,
             *                                         "elementId": 1,
             *                                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",
             *                                         "score":6.0,
             *                                     },
             *                                     {
             *                                         "id": 2,
             *                                         "elementId": 1,
             *                                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
             *                                         "score":-1.0,
             *                                     },
             *                                     {
             *                                         "id": 3,
             *                                         "elementId": 1,
             *                                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
             *                                         "score":3.0,
             *                                     }
             *                                 ],
             *                                 "criteria": "是否合格，不设分值",
             *                                 "type": 0
             *                             }
             *                         ]
             *                     }
             *                 ]
             *         ],
             *         "totalCount": 1,
             *         "totalPageCount": 1,
             *         "pageIndex": 0,
             *         "futureCount": {
             *             "cancelled": false,
             *             "done": true
             *         },
             *         "pageSize": 10
             *     }
             * @apiSuccess (Success 200){String[]} reason []
             *
             * @apiSuccess (Error 500){int} code 500
             * @apiSuccess (Error 500){Object[]} data null
             * @apiSuccess (Error 500){String[]} reason 错误信息
             */
    public CompletionStage<Result> getElementList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            //条件
            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());
            String element=(jsonNode.findPath("element") instanceof MissingNode ?null:jsonNode.findPath("element").asText());
            String criteria=(jsonNode.findPath("criteria") instanceof MissingNode ?null:jsonNode.findPath("criteria").asText());
            Integer type=(jsonNode.findPath("type") instanceof MissingNode ?null:jsonNode.findPath("type").asInt());
            Double score=(jsonNode.findPath("score") instanceof MissingNode ?null:jsonNode.findPath("score").asDouble());
            Long indicatorId=(jsonNode.findPath("indicatorId") instanceof MissingNode ?null:jsonNode.findPath("indicatorId").asLong());

            ExpressionList<Element> expressionList = Element.find.query().where();
            if(userId!=null){
                Set<Long> elementIds = TeacherElementScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherElementScore::getElementId).collect(Collectors.toSet());
                expressionList.in("id",elementIds);
            }
            if(id!=null&&id>0) expressionList.eq("id",id);
            if(element!=null&&!element.isEmpty()) expressionList.icontains("element",element);
            if(criteria!=null&&!criteria.isEmpty()) expressionList.icontains("criteria",criteria);
            if(type!=null&&type>0) expressionList.eq("type",type);
            if(score!=null&&score>0) expressionList.eq("score",score);
            if(indicatorId!=null&&indicatorId>0) expressionList.eq("indicator_id",indicatorId);

            Pair<PagedList<Element>, List<String>> list = v3TeacherRepository.getElementList(expressionList,currentPage,pageSize);

            ObjectNode node = Json.newObject();
            node.put("code", CODE200);
            node.set("list",Json.toJson(list.first().getList()));
            node.put("pages", list.first().getTotalPageCount());
            return ok(node);
        });
    }

    /**
     * @api {POST} /v1/tk/getContentList/  07 获取所有Content
     * @apiName getContentList
     * @apiGroup Teacher
     *
     * @apiDescription 获取所有Content的信息，也可获得对应用户的内容，也可以获取对应要素的内容
     *
     * @apiParam {int} currentPage 当前页面
     * @apiParam {int} pageSize 显示条数
     * @apiParam {Long} userId (可选)用户ID
     * @apiParam {Long} elementId (可选)要素ID
     *
     * @apiParamExample {json} 请求示例:
     *       {
     *         "currentPage":1,
     *         "pageSize":10,
     *         "userId":1,//用户ID，会查询该用户的评价内容
     *         "elementId":1//要素ID，会查询该评价要素对应的内容
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccessExample {json} data 数据详情
     * {
     *     "code": 200,
     *     "data": {
     *         "list": [
     *             {
     *                 "id": 1,//要素ID
     *                 "indicatorId": 1,//指标ID
     *                 "element": null,//要素名称
     *                 "contentList": [
     *                     {
     *                         "id": 1,//内容ID
     *                         "elementId": 1,//要素ID
     *                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",//内容名称
     *                         "score":6.0,//该内容加权得分
     *                     },
     *                     {
     *                         "id": 2,
     *                         "elementId": 1,
     *                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
     *                         "score":-1.0
     *                     },
     *                     {
     *                         "id": 3,
     *                         "elementId": 1,
     *                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
     *                         "score":3.0
     *                     }
     *                 ],
     *                 "criteria": "是否合格，不设分值",//标准
     *                 "type": 0//类型
     *             },
     *             {
     *                 "id": 2,
     *                 "indicatorId": 2,
     *                 "element": "1.出勤情况",
     *                 "contentList": [
     *                     {
     *                         "id": 4,
     *                         "elementId": 2,
     *                         "content": "病假"
     *                     },
     *                     {
     *                         "id": 5,
     *                         "elementId": 2,
     *                         "content": "事假"
     *                     },
     *                     {
     *                         "id": 6,
     *                         "elementId": 2,
     *                         "content": "迟到"
     *                     },
     *                     {
     *                         "id": 7,
     *                         "elementId": 2,
     *                         "content": "旷课"
     *                     },
     *                     {
     *                         "id": 8,
     *                         "elementId": 2,
     *                         "content": "政治学习"
     *                     },
     *                     {
     *                         "id": 9,
     *                         "elementId": 2,
     *                         "content": "教研活动"
     *                     },
     *                     {
     *                         "id": 10,
     *                         "elementId": 2,
     *                         "content": "学校会议及其他集体活动出勤情况"
     *                     }
     *                 ],
     *                 "criteria": null,
     *                 "type": 0
     *             }
     *         ],
     *         "pageIndex": 0,
     *         "futureCount": {
     *             "cancelled": false,
     *             "done": false
     *         },
     *         "pageSize": 10,
     *         "totalPageCount": 2,
     *         "totalCount": 20
     *     },
     *     "reason": []
     * }
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){Object[]} data null
     * @apiSuccess (Error 500){String[]} reason 错误信息
     */
    public CompletionStage<Result> getContentList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Long elementId=(jsonNode.findPath("elementId") instanceof MissingNode ?null:jsonNode.findPath("elementId").asLong());
            //条件
            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());
            String content=(jsonNode.findPath("content") instanceof MissingNode ?null:jsonNode.findPath("content").asText());
            Double score=(jsonNode.findPath("score") instanceof MissingNode ?null:jsonNode.findPath("score").asDouble());

            ExpressionList<Content> expressionList = Content.find.query().where();
            if(userId!=null&&userId>0){
                Set<Long> contentIds = TeacherContentScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherContentScore::getElementId).collect(Collectors.toSet());
                expressionList.in("id",contentIds);
            }
            if(elementId!=null&&elementId>0){
                expressionList.eq("element_id",elementId);
            }
            if(id!=null&&id>0){
                expressionList.eq("id",id);
            }
            if(content!=null&&!content.isEmpty()){
                expressionList.icontains("content",content);
            }
            if(score!=null&&score!=0.0){
                expressionList.eq("score",score);
            }
            Pair<PagedList<Content>, List<String>> list = v3TeacherRepository.getContentList(expressionList,currentPage,pageSize);

            //分页内数据
            List<Content> dataList = list.first().getList();
            //错误信息
            List<String> errorMsg = list.second();
            //分页查询分页信息
            PagedList<Content> pageList = list.first();

            ObjectNode node = Json.newObject();
            node.put("code", CODE200);
            node.set("list",Json.toJson(list.first().getList()));
            node.put("pages", list.first().getTotalPageCount());
            return ok(node);
        });
    }

    /**
     * @api {POST} /v1/tk/dispatch/  08 考核规则下发
     * @apiName dispatch
     * @apiGroup Teacher
     *
     * @apiDescription 下发考核给相应教师
     *
     * @apiParam {String} ids 接收的教师id，用,分割
     * @apiParam {Long} kpiId 下发的考核KPI的ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *       "ids":"2,3",//需要下发的教师ID
     *       "kpiId":1//对应的kpiID
     * }
     *
     * @apiSuccess (Error 404) {String} msg ids未给出
     * @apiSuccess (Error 404) {String} msg kpiId未给出
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} reason 错误列表
     */
    public CompletionStage<Result> dispatch(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            List<Long> ids = jsonNode.findPath("ids") instanceof MissingNode ? null : Arrays.stream(jsonNode.findPath("ids").asText().split(",")).map(String::trim).map(Long::valueOf).collect(Collectors.toList());
            Long kpiId= jsonNode.findPath("kpiId") instanceof MissingNode ? null : jsonNode.findPath("kpiId").asLong();

            if(ids==null) return ok("ids未给出");
            if(kpiId==null) return ok("kpiId未给出");

            List<User> userList = User.find.query().where().in("id",ids).findList();
            userList.forEach(user->{
                user.setDispatchIds(user.getDispatchIds()+"+"+kpiId+"+");
            });
            try(Transaction transaction=User.find.db().beginTransaction()){
                DB.updateAll(userList);
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(5000,"更新状态失败,出错:"+e);
            }

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.dispatchAll(ids, kpiId);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    //new
    /**
     * @api {POST} /v1/tk/grade/  09 评分
     * @apiName grade
     * @apiGroup Teacher
     *
     * @apiDescription 教师评分
     *
     * @apiParam {Long} userId 教师ID
     * @apiParam {Long} contentId 内容ID
     * @apiParam {Integer} time 次数
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,//用户ID
     *     "tcs":[//教师评分对应的内容
     *         {
     *             "contentId":1,//内容ID
     *             "time":1,//次数
     *             "fileId":1
     *         },
     *         {
     *             "contentId":2,
     *             "time":1,
     *             "fileId":2
     *         },
     *         {
     *             "contentId":3,
     *             "time":2,
     *             "fileId":3
     *         }
     *     ]
     * }
     *
     * @apiSuccess (Error 404) {String} msg 没有userId
     * @apiSuccess (Error 404) {String} msg tcs为空
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} reason 错误列表
     */
    public CompletionStage<Result> grade(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId= jsonNode.findPath("userId") instanceof MissingNode ? null : jsonNode.findPath("userId").asLong();
            List<TeacherContentScore> teacherContentScores= objectMapper.convertValue(jsonNode.findPath("tcs"), new TypeReference<>() {});

            if(userId==null) return ok("没有userId");
            if(teacherContentScores==null) return ok("tcs为空");

            Pair<Boolean, List<String>> grade = v3TeacherRepository.grade(userId, teacherContentScores);



            return okCustomNode(grade.first(),grade.second());
        });
    }

    /**
     * @api {POST} /v1/tk/export/  10 导出
     * @apiName export
     * @apiGroup Teacher
     *
     * @apiDescription 导出相应教师的KPI的PDF
     *
     * @apiParam {Long} userId 教师ID
     * @apiParam {Long} kpiId KPI的ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,//用户ID
     *     "kpiId":1//KpiID
     * }
     *
     * @apiSuccess (Success 200){File} file PDF文件
     *
     */
    public CompletionStage<Result> export(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId= jsonNode.findPath("userId") instanceof MissingNode ? null : jsonNode.findPath("userId").asLong();
            Long kpiId= jsonNode.findPath("kpiId") instanceof MissingNode ? null : jsonNode.findPath("kpiId").asLong();

            if(userId==null) return ok("userId为空");
            if(kpiId==null) return ok("kpiId为空");

            User user = User.find.query().where()
                    .eq("id",userId)
                    .setMaxRows(1).findOne();
            if(user==null) return okCustomJson(CODE40001,"没有该用户");
            TeacherKPIScore tsk = TeacherKPIScore.find.query().where()
                    .eq("user_id", userId)
                    .eq("kpi_id", kpiId)
                    .setMaxRows(1).findOne();
            if(tsk==null) return okCustomJson(CODE40001,"该用户没有下发的评分");

            if(is_DEV){
                try {
                    this.testPDF();
                    return ok("complete");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                byte[] pdfBytes = AssessmentPDF.v3ExportToPdf(userId, kpiId);

                String filename="teacher_perform_"+userId+"_"+kpiId;

                // 创建临时文件
                File file = File.createTempFile(filename, ".pdf");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(pdfBytes);
                }

                return ok(file)
                        .withHeader("content-disposition", "attachment; filename=\"" + filename + "\"")
                        .as( "application/pdf");
            } catch (IOException e) {
                return ok("导出失败"+e);
            }

        });
    }

    /**
     * @api {POST} /v1/tk/auto/  11 批量设置指标计算模式
     * @apiName isAutoCalculator
     * @apiGroup Teacher
     *
     * @apiDescription 通过要素ID批量设置要素为系统自动计算
     *
     * @apiParam {String} elementIds 要素ID集合
     * @apiParam {Integer} type 类型  0:手动计算,1:自动计算,2:提交上报(不填默认手动)
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementIds":"1,2,3",//要素ID
     *     "type":1//类型
     * }
     *
     * @apiSuccess (Error 404) {String} msg elementIds为空
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} msg 更新成功
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} msg 更新失败 异常信息
     */
    public CompletionStage<Result> isAutoCalculator(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Long> elementIds=(jsonNode.findPath("elementIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("elementIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());
            Integer type=(jsonNode.findPath("type") instanceof MissingNode ?0: jsonNode.findPath("type").asInt());

            if(elementIds==null) return ok("elementIds为空");

            List<Element> elementList=Element.find.query().where().in("id",elementIds).findList();
            elementList.forEach(element -> {
                element.setType(type);
            });

            ObjectNode result = Json.newObject();
            try(Transaction transaction= DB.beginTransaction()){
                result.put("code",200);
                result.put("msg","更新成功");
                transaction.commit();
                return ok(result);
            }catch (Exception e){
                result.put("code",500);
                result.put("msg","更新失败 "+e);
                return ok(result);
            }
        });
    }

    /**
     * @api {POST} /v1/tk/delete/  12 多功能删除
     * @apiName deleteElementOrContentOrIndicatorOrKpi
     * @apiGroup Teacher
     *
     * @apiDescription 可以删除element,content,indicator,kpi，注意越高等级的会连带将低等级的删
     *
     * @apiParam {String} elementIds (可选)要素删除
     * @apiParam {String} contentIds (可选)内容删除
     * @apiParam {String} indicatorIds (可选)指标删除
     * @apiParam {String} kpiIds (可选)kpi删除
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementIds":"1,2,3",//要素ID
     *     "contentIds":"1",//内容ID
     * }
     * @apiSuccess (Error 404) {String} msg 没有需要删除的id
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){String[]} reason 错误列表
     */
    public CompletionStage<Result> deleteElementOrContentOrIndicatorOrKpi(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Long> elementIds=(jsonNode.findPath("elementIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("elementIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());
            List<Long> contentIds=(jsonNode.findPath("contentIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("contentIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());
            List<Long> indicatorIds=(jsonNode.findPath("indicatorIds") instanceof MissingNode ? null : Arrays.stream(jsonNode.findPath("indicatorIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());
            List<Long> kpiIds=(jsonNode.findPath("kpiIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("kpiIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());

            if(elementIds==null&&contentIds==null&&indicatorIds==null&&kpiIds==null){
                return ok("没有需要删除的id");
            }

            List<KPI> deletekpiList=null;
            List<Indicator> deleteIndicatorList=null;
            List<Element> deleteElementList=null;
            List<Content> deleteContentList=null;

            if(kpiIds!=null){
                deletekpiList = KPI.find.query().where().in("id", kpiIds).findList();
                List<Long> ids = Indicator.find.query().where().in("kpi_id",kpiIds).findList().stream().map(Indicator::getId).toList();

                if(indicatorIds != null) indicatorIds.addAll(ids);
                else indicatorIds=new ArrayList<>(ids);
            }
            if(indicatorIds!=null){
                deleteIndicatorList=Indicator.find.query().where().in("id", indicatorIds).findList();
                List<Long> ids = Element.find.query().where().in("indicator_id",indicatorIds).findList().stream().map(Element::getId).toList();

                if(elementIds != null) elementIds.addAll(ids);
                else elementIds=new ArrayList<>(ids);
            }
            if(elementIds!=null){
                deleteElementList=Element.find.query().where().in("id", elementIds).findList();
                List<Long> ids = Content.find.query().where().in("element_id",elementIds).findList().stream().map(Content::getId).toList();

                if(contentIds != null) contentIds.addAll(ids);
                else contentIds=new ArrayList<>(ids);
            }
            deleteContentList=Content.find.query().where().in("id",contentIds).findList();

            List<String> errMsg=new ArrayList<>();
            Transaction transaction=DB.beginTransaction();
            try{
                if(deletekpiList!=null) DB.deleteAll(deletekpiList);
                else errMsg.add("warning---KPI为空");
            }catch (Exception e){
                errMsg.add("删除KPI出错");
                transaction.rollback();
            }
            try{
                if(deleteIndicatorList!=null) DB.deleteAll(deleteIndicatorList);
                else errMsg.add("warning---Indicator为空");
            }catch (Exception e){
                errMsg.add("删除Indicator出错");
                transaction.rollback();
            }
            try{
                if(deleteElementList!=null) DB.deleteAll(deleteElementList);
                else errMsg.add("warning---Element为空");
            }catch (Exception e){
                errMsg.add("删除Element出错");
                transaction.rollback();
            }
            try{
                DB.deleteAll(deleteContentList);
                transaction.commit();
            }catch (Exception e){
                errMsg.add("删除Content出错");
                transaction.rollback();
            }
            return okCustomNode(errMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errMsg);
        });
    }

    /**
     * @api {POST} /v1/tk/withDraw/  13 撤销下发
     * @apiName withDraw
     * @apiGroup Teacher
     *
     * @apiDescription 撤销教师的评级
     *
     * @apiParam {String} teacherIds
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "teacherIds":"1,2,3",//撤销的用户ID
     *     "kpiId":1
     * }
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){msg} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){msg} reason 错误信息
     *
     * @apiSuccess (Error 404){int} msg 没有教师ID
     */
    public CompletionStage<Result> withDraw(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Long> teacherIds=(jsonNode.findPath("teacherIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("teacherIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null: jsonNode.findPath("kpiId").asLong());

            if(teacherIds==null) return ok("没有教师ID");

            List<User> userList=User.find.query().where().in("id",teacherIds).findList();
            userList.forEach(user->{
                if(kpiId!=null){
                    user.setDispatchIds(user.getDispatchIds().replace("+"+kpiId+"+", ""));
                }else{
                    user.setDispatchIds(null);
                }
            });
            try(Transaction transaction = User.find.db().beginTransaction()){
                DB.updateAll(userList);
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(5000,"撤销出错："+e);
            }

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.withDraw(teacherIds);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    /**
     * @api {POST} /v1/tk/post/  14 提交审核
     * @apiName postAudit
     * @apiGroup Teacher
     *
     * @apiDescription 提交给领导
     *
     * @apiParam {Long} userId 用户ID
     * @apiParam {String} LeaderIds 领导ID，用,号分割
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,//发起人ID
     *     "LeaderIds":"1,2,3",//发给的领导ID
     *     "elementId":1
     * }
     *
     * @apiSuccess (Error 404) {int} msg LeaderIds为空
     * @apiSuccess (Error 404) {int} msg userId为空
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {String[]} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500) {String[]} reason 错误列表
     */
    public CompletionStage<Result> postAudit(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            String LeaderIds=(jsonNode.findPath("LeaderIds") instanceof MissingNode ?null: jsonNode.findPath("LeaderIds").asText());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Long elementId=(jsonNode.findPath("elementId") instanceof MissingNode ?null:jsonNode.findPath("elementId").asLong());

            if(LeaderIds==null) return ok("LeaderIds为空");
            if(userId==null) return ok("userId为空");

            if(elementId!=null&&elementId>0){
                TeacherElementScore tes = TeacherElementScore.find.query().where().eq("user_id", userId).eq("element_id", elementId).setMaxRows(1).findOne();
                TeacherTask teacherTask = new TeacherTask();
                List<String> errMsg=new ArrayList<>();
                boolean status=true;
                teacherTask.setUserId(userId);
                teacherTask.setParentIds(LeaderIds);
                teacherTask.setStatus("待完成");
                teacherTask.setTesId(tes!=null?tes.getId():null);
                try(Transaction transaction = DB.beginTransaction()){
                    teacherTask.save();
                    transaction.commit();
                } catch (Exception e) {
                    status=false;
                    errMsg.add("添加审核任务出错: "+e);
                }
                return okCustomNode(status,errMsg);
            }
            else{
                List<TeacherElementScore> teacherElementScoreList = TeacherElementScore.find.query().where().eq("user_id",userId).findList();
                List<TeacherTask> addTeacherTaskList=new ArrayList<>();
                List<String> errMsg=new ArrayList<>();
                boolean status=true;
                teacherElementScoreList.forEach(tes->{
                    TeacherTask teacherTask=new TeacherTask();
                    teacherTask.setUserId(userId);
                    teacherTask.setParentIds(LeaderIds);
                    teacherTask.setStatus("待完成");
                    teacherTask.setTesId(tes.getId());
                    addTeacherTaskList.add(teacherTask);
                });
                try(Transaction transaction = DB.beginTransaction()){
                    DB.saveAll(addTeacherTaskList);
                    transaction.commit();
                } catch (Exception e) {
                    status=false;
                    errMsg.add("添加审核任务出错: "+e);
                }
                return okCustomNode(status,errMsg);
            }
        });
    }

    /**
     * @api {POST} /v1/tk/get/leader/  15 获取所有领导
     * @apiName withDraw
     * @apiGroup Teacher
     *
     * @apiDescription 获取所有领导
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object} data 所有领导的数据
     * @apiSuccessExample {json} 数据展示:
     * {
     *     "id":1,//用户ID
     *     "userName":"zs",//用户名称
     *     "typeName":"校长",//领导职位
     *     "status":1,//账号状态
     *     "roleId":3,//角色ID
     *     "role":{
     *         "id":3,//角色ID
     *         "nickName":"领导"//角色名称
     *     }
     * }
     */
    public CompletionStage<Result> getLeader(){
        return CompletableFuture.supplyAsync(()->{
            List<Role> roles = Role.find.query().where()
                    .or()
                        .eq("nick_name", "领导")
                        .eq("nick_name","管理员")
                    .endOr()
                    .findList();
            List<Long> roleIds = Objects.requireNonNull(roles).stream().map(Role::getId).toList();

            List<User> leadersList = User.find.query().where().in("role_id", roleIds).findList();
            leadersList.forEach(leaders->{
                Role role = roles.stream().filter(v1 -> Objects.equals(leaders.getRoleId(), v1.getId())).findFirst().orElse(null);
                leaders.setRole(role);
            });

            return okCustomNode(true,null,leadersList);
        });
    }

    /**
     * @api {POST} /v1/tk/get/leader/task/  16 获取上级的任务
     * @apiName getToDoTask
     * @apiGroup Teacher
     *
     * @apiDescription 获取该用户的代办任务
     *
     * @apiParam {Long} userId 用户ID
     * @apiParamExample {json} 示例:
     * {
     *     "userId":1,
     * }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){msg} reason null
     * @apiSuccess (Success 200){msg} data 数据
     * @apiSuccessExample {json} 数据:
     * {
     *     "id":1,
     *     "userId":1,//发送人ID
     *     "userName":"zs",//发送人姓名
     *     "parentIds":"1,2,3",//上级用户ID
     *     "status":"待完成",//状态 已完成、待完成
     *     "tesId":1,//教师下发评分要素ID
     *     "teacherElementScore":{//教师下发评分要素
     *          "id":1,
     *          "userId":1,//发送教师的ID
     *          "elementId":1,//对应的要素ID
     *          "KpiId":1,//对应的kpiId
     *          "score":9.0,//该要素的初始总分
     *          "task_id":1,//对应数据的任务ID
     *          "finalScore":10.0//该要素的最终得分
     *     }
     * }
     *
     * @apiSuccess (Error 404){int} msg userId为空
     */
    public CompletionStage<Result> getToDoTask(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(userId==null) return ok("userId为空");
            //条件
            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());
            String status=(jsonNode.findPath("status") instanceof MissingNode ?null:jsonNode.findPath("status").asText());
            Long childId=(jsonNode.findPath("childId") instanceof MissingNode ?null:jsonNode.findPath("childId").asLong());

            ExpressionList<TeacherTask> expressionList = TeacherTask.find.query().where();
            if(id!=null&&id>0) expressionList.eq("id",id);
            if(status!=null&&!ValidationUtil.isEmpty(status)) expressionList.icontains("status", status);
            if(childId!=null&&childId>0) expressionList.eq("user_id",childId);

            List<TeacherTask> leaderTeacherTaskList=expressionList
                    .or()
                    .eq("parent_ids", userId)
                    .like("parent_ids", userId + ",%")
                    .like("parent_ids", "%," + userId)
                    .like("parent_ids", "%," + userId + ",%")
                    .endOr().findList();
            List<User> AllUserList = User.find.query().where()
                    .findList();

            //评分任务对应的教师评分要素Id
            List<Long> tesIds = leaderTeacherTaskList.stream().map(TeacherTask::getTesId).toList();
            //评分任务Id
            List<Long> ttIds = leaderTeacherTaskList.stream().map(TeacherTask::getUserId).toList();
            List<TeacherElementScore> teslist = TeacherElementScore.find.query().where()
                    .in("id", tesIds)
                    .findList();
            List<User> userList = AllUserList.stream()
                    .filter(v1->ttIds.contains(v1.getId())).toList();

            //对应的要素Id
            List<Long> elementIds = teslist.stream().map(TeacherElementScore::getElementId).toList();
            List<Element> elementList=Element.find.query().where()
                    .in("id",elementIds)
                    .findList();


            leaderTeacherTaskList.forEach(tt->{
                TeacherElementScore teacherElementScore = teslist.stream()
                        .filter(tes -> Objects.equals(tt.getTesId(), tes.getId()))
                        .findFirst()
                        .orElse(null);
                tt.setTeacherElementScore(teacherElementScore);

                String userName = Objects.requireNonNull(userList.stream().
                        filter(user -> Objects.equals(tt.getUserId(), user.getId()))
                        .findFirst()
                        .orElse(null))
                        .getUserName();
                tt.setUserName(userName);

                if(teacherElementScore!=null){
                    Element element = elementList.stream()
                            .filter(v1 -> Objects.equals(v1.getId(), teacherElementScore.getElementId()))
                            .findFirst()
                            .orElse(null);
                    tt.setElement(element);
                }
                List<String> leaderUserNameList = AllUserList.stream().filter(v1 -> tt.getParentIds().contains(String.valueOf(v1.getId()))).map(User::getUserName).toList();
                tt.setParentName(String.join(",", leaderUserNameList));
            });

            return okCustomNode(true,null,leaderTeacherTaskList);
        });
    }

    /**
     * @api {POST} /v1/tk/add/leader/score/  17 上级评分
     * @apiName addLeaderScore
     * @apiGroup Teacher
     *
     * @apiDescription 用于最终评分
     *
     * @apiParam {Long} id 任务ID
     * @apiParam {Long} tesId 要素ID
     * @apiParam {Double} score 分数
     * @apiParamExample {json} 请求示例:
     * {
     *      "id":1,//任务ID
     *      "data":{
     *          "tesId":1,//该教师的评测要素ID
     *          "score":12.0//最终得分
     *      }
     * }
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){msg} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){msg} reason 错误信息
     *
     * @apiSuccess (Error 404){int} msg id为空
     * @apiSuccess (Error 404){int} msg data为空
     */
    public CompletionStage<Result> addLeaderScore(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());
            Map<String,Object> dataMap=objectMapper.convertValue(jsonNode.findPath("data"), new TypeReference<>() {});

            if(id==null) return ok("id为空");
            if(dataMap==null) return ok("data为空");

            List<String> errorMsg=new ArrayList<>();
            Transaction transaction=DB.beginTransaction();

            TeacherTask teacherTask = TeacherTask.find.query().where()
                    .eq("id",id)
                    .setMaxRows(1)
                    .findOne();
            if(teacherTask==null) return okCustomJson(40001,"没有该任务");
            teacherTask.setStatus("已完成");
            try{
                teacherTask.update();
            }catch (Exception e){
                errorMsg.add("更新任务出错: "+e);
                transaction.rollback();
            }

            TeacherElementScore tes = TeacherElementScore.find.query().where()
                    .eq("id", teacherTask.getTesId())
                    .setMaxRows(1)
                    .findOne();
            if(tes==null) return okCustomJson(40001,"该教师没有该要素");
            if(tes.getScore()==null) tes.setScore(0.0);
            tes.setFinalScore(Double.valueOf(dataMap.get("score").toString()));
            try{
                tes.update();
            }catch (Exception e){
                errorMsg.add("更新最终得分出错: "+e);
                transaction.rollback();
            }

            TeacherIndicatorScore tis = TeacherIndicatorScore.find.query().where()
                    .eq("indicator_id", tes.getIndicatorId())
                    .setMaxRows(1)
                    .findOne();
            if(tis==null) return okCustomJson(40001,"该教师没有该指标");
            if(tis.getScore()==null) tis.setScore(0.0);
            tis.setScore(tis.getScore()-tes.getScore()+tes.getFinalScore());
            try{
                tis.update();
            }catch (Exception e){
                errorMsg.add("更新指标得分出错: "+e);
                transaction.rollback();
            }

            TeacherKPIScore tks = TeacherKPIScore.find.query().where()
                    .eq("kpi_id",tis.getKpiId())
                    .setMaxRows(1)
                    .findOne();
            if(tks==null) return okCustomJson(40001,"该教师没有该KPI");
            if(tks.getScore()==null) tks.setScore(0.0);
            tks.setScore(tks.getScore()-tes.getScore()+tes.getFinalScore());
            try{
                tks.update();
                transaction.commit();
            }catch (Exception e){
                errorMsg.add("更新KPI得分出错: "+e);
                transaction.rollback();
            }

            return okCustomNode(true,errorMsg);
        });
    }

    /**
     * @api {POST} /v1/tk/add/rule/import/  18 导入文件
     * @apiName importFile
     * @apiGroup Teacher
     *
     * @apiDescription 导入规则文件自动入库
     *
     * @apiParam {File} file 文件
     *
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccessExample {json} 请求示例:
     *{
     *     "success": true,//状态
     *     "message": "成功解析 2 个表格",//信息
     *     "fileName": "test.docx",//文件名
     *     "fileType": "WORD_DOCX",//文件类型
     *     "totalTables": 2,//总共的表格数
     *     "tables": [//解析的表格信息
     *         {
     *             "fileName": "test.docx",
     *             "sheetName": null,
     *             "tableIndex": 0,
     *             "headers": [
     *                 "评价指标",
     *                 "评价要素",
     *                 "评 价 内 容",
     *                 "评 价 标 准",
     *                 "初 始 得 分（100分）",
     *                 "最 终 得 分（100分）"
     *             ],
     *             "rows": [
     *                 [
     *                     "一   二   三级   级   级",
     *                     "",
     *                     "",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "师德师风（优、合格、不合格）",
     *                     "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行",
     *                     "是否合格，不设分值",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "教育教学常规30-40 分",
     *                     "1.出勤情况",
     *                     "病假、事假、迟到、旷课、政治学习、教研活动、学校会议及其他集体活动出勤情况",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.课时工作量",
     *                     "完成标准课时工作量、代课、特殊课时等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.班主任等工作",
     *                     "担任班主任、年段长、教研组长、少先队总辅导员、团委书记及中层以上干部工作",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "4.课堂教学",
     *                     "制定教学计划、组织教学、课堂管理、教学理念、课堂实效、德育渗透、现代教育技术手段与学科融合等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "5.学生发展指导",
     *                     "作业批改、个性化辅导、心理辅导、学生综合素质评定、成长档案记录、帮扶学困生（特殊生）生涯规划指导,五育并举指导,学习行为习惯培养等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "6.家校联系",
     *                     "家长会、家访、家教指导、家长学校培训等",
     *                     "",
     *                     "",
     *                     ""
     *                 ]
     *             ],
     *             "rowCount": 10,
     *             "columnCount": 6
     *         },
     *         {
     *             "fileName": "test.docx",
     *             "sheetName": null,
     *             "tableIndex": 1,
     *             "headers": [
     *                 "评价指标",
     *                 "评价要素",
     *                 "评 价 内 容",
     *                 "评 价 标 准",
     *                 "初 始 得 分（100分）",
     *                 "最 终 得 分（100分）"
     *             ],
     *             "rows": [
     *                 [
     *                     "一   二   三级   级   级",
     *                     "",
     *                     "",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "教育教学业绩30-45 分",
     *                     "1.教育教学业绩",
     *                     "任教班级学生学业发展情况、任教学科中高考、合格性考试（会考）、市质检、期末考试、质量监测等的平均分、及格率、优秀率率",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.示范引领",
     *                     "履行师徒协议及指导成效，承担各级学科研训组、名师工作室工作、担任学科培训班导师，参加“送教送培下乡”活动等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.任教班级获奖",
     *                     "班（团,队）会活动、单项竞赛获奖、综合性荣誉表彰",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "4.指导学生获奖情况",
     *                     "指导学生参加学科竞赛、创新大赛、体育联赛、艺术展演等获奖",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "5.校本课程、综合实践活动",
     *                     "参与校本课程研发，指导研究性学习，组织综合实践、社会实践、社团活动，担任课外活动辅导员等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "6.教学团队合作",
     *                     "参与教研、集备活动,教案、课件资源共享，参加岗位练兵、培训学习，完成听评课任务等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "7.学生（家长）评价",
     *                     "学生、家长满意率",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "个人专业发展5-15 分",
     *                     "1.承担公开教学活动",
     *                     "开设各级学科示范课、观摩课及专题讲座,主持班（团/队）观摩活动、区域交流活动等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.撰写教育教学论文",
     *                     "撰写学科、德育、党建、管理论文获奖或收入校级以上汇编",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "3.参与教育教学研究",
     *                     "参与各级学科、德育、管理的课题研究、专题教研活动、项目式研究活动,负责学科命题,完成继续教育",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "4.个人获奖",
     *                     "获得各级教育教学专项表彰、综合性荣誉表彰、公开教学竞赛获奖、名优骨干认定",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "承担急难险重工作5-10 分",
     *                     "1.承担学校行政工作",
     *                     "承担学校党建/教研/工会/后勤/安全/宣传/财务等工作，如集备组长/支部书记/党务秘书/安全专干/财务人员/实验组组长/处室干事等",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "",
     *                     "2.承担学校临时性重要任务",
     *                     "承担创建、评估、迎检、校庆、志愿服务等工作",
     *                     "",
     *                     "",
     *                     ""
     *                 ],
     *                 [
     *                     "总 分   100 分",
     *                     ""
     *                 ]
     *             ],
     *             "rowCount": 15,
     *             "columnCount": 6
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> importFile(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            try {
                Http.MultipartFormData<play.libs.Files.TemporaryFile> body = request.body().asMultipartFormData();
                Http.MultipartFormData.FilePart<play.libs.Files.TemporaryFile> filePart = body.getFile("file");

                if (filePart == null) {
                    return badRequest(createErrorResponse("请选择要上传的文件"));
                }

                String fileName = filePart.getFilename();
                play.libs.Files.TemporaryFile tempFile = filePart.getRef();

                // 验证文件
                if (fileName.isEmpty()) {
                    return badRequest(createErrorResponse("文件名不能为空"));
                }

                if (!fileParseService.isSupportedFile(fileName)) {
                    return badRequest(createErrorResponse(
                            String.format("不支持的文件格式: %s。支持: .xlsx, .xls, .docx, .doc", fileName)
                    ));
                }

                if (tempFile.path().toFile().length() > 10 * 1024 * 1024) {
                    return badRequest(createErrorResponse("文件大小不能超过 10MB"));
                }

                // 解析文件
                List<WordTableTitleExtractor.TableTitleInfo> tableTitleInfos = WordTableTitleExtractor.extractTableTitles(tempFile.path().toFile(), fileName);
                if(!tableTitleInfos.isEmpty()){
                    WordParser.setRuleTitle(tableTitleInfos.get(0).getTitleText());
                }
                ParseResult result = fileParseService.parseTemporaryFile(tempFile, fileName);

                if (result.isSuccess()) {
                    return ok(createSuccessResponse(result));
                } else {
                    return badRequest(createErrorResponse(result.getMessage()));
                }

            } catch (Exception e) {
                return internalServerError(createErrorResponse("服务器内部错误: " + e.getMessage()));
            }
        });
    }

    /**
     * @api {POST} /v1/tk/update/  19 多功能更新
     * @apiName updateElementOrContentOrIndicatorOrKpi
     * @apiGroup Teacher
     *
     * @apiDescription 可以更新element,content,indicator,kpi所对应的信息
     *
     * @apiParam {Object} element (可选)要素
     * @apiParamExample {json} 要素类结构示例:
     * {
     *      "id":1,//要素ID
     *      "element":"123",//要素名称
     *      "criteria":"2345",//标准
     *      "type":0//类型
     * }
     *
     * @apiParam {Object} content (可选)内容
     * @apiParamExample {json} 内容类结构示例:
     * {
     *      "id":1,//内容ID
     *      "content":"123"//内容名称
     * }
     *
     * @apiParam {Object} indicator (可选)指标
     * @apiParamExample {json} 指标类结构示例:
     * {
     *      "id":1,//指标ID
     *      "indicatorName":"123",//指标名称
     *      "subName":"22344"//指标附属名称(在名字下方的)
     * }
     *
     * @apiParam {Object} kpi (可选)kpi
     * @apiParamExample {json} kpi类结构示例:
     * {
     *      "id":1,//kpiID
     *      "title":"123"//kpi标题
     * }
     *
     * @apiParam {Int} type 类型(1:要素,2:内容,3:指标,4:kpi)
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "type":2,//类型选择
     *     "data":[//这里填入对应的类结构，我这里是内容
     *          {
     *              "id":62,
     *              "content":"学生"
     *          },
     *          {
     *              "id":63,
     *              "content":"家长满意率"
     *          }
     *     ]
     * }
     * @apiSuccess (Error 404) {int} msg 没有type
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {String[]} reason 更新成功
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500) {String[]} reason 错误列表
     */
    public CompletionStage<Result> updateElementOrContentOrIndicatorOrKpi(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Integer type=(jsonNode.findPath("type") instanceof MissingNode ?null:jsonNode.findPath("type").asInt());

            if(type==null) return ok("没有type");

            System.out.println(type);
            if(type==1){
                List<Element> elementList=objectMapper.convertValue(jsonNode.findPath("data"), new TypeReference<>() {});
                try(Transaction transaction=Element.find.db().beginTransaction()){
                    DB.updateAll(elementList);
                    transaction.commit();
                }catch (Exception e){
                    return okCustomNode(false,List.of("更新要素出错: "+e));
                }
            } else if (type==2) {
                List<Content> contentList=objectMapper.convertValue(jsonNode.findPath("data"), new TypeReference<>() {});
                try(Transaction transaction=Content.find.db().beginTransaction()){
                    DB.updateAll(contentList);
                    transaction.commit();
                }catch (Exception e){
                    return okCustomNode(false,List.of("更新内容出错: "+e));
                }
            } else if (type==3) {
                List<Indicator> indicatorList=objectMapper.convertValue(jsonNode.findPath("data"), new TypeReference<>() {});
                try(Transaction transaction=Indicator.find.db().beginTransaction()){
                    DB.updateAll(indicatorList);
                    transaction.commit();
                }catch (Exception e){
                    return okCustomNode(false,List.of("更新指标出错: "+e));
                }
            } else if (type==4) {
                List<KPI> kpiList=objectMapper.convertValue(jsonNode.findPath("data"), new TypeReference<>() {});
                try(Transaction transaction=KPI.find.db().beginTransaction()){
                    DB.updateAll(kpiList);
                    transaction.commit();
                }catch (Exception e){
                    return okCustomNode(false,List.of("更新kpi出错: "+e));
                }
            }else{
                return ok("type不正确");
            }
            return okCustomNode(true,List.of("更新成功"));
        });
    }

    /**
     * @api {POST} /v1/tk/update/chain/  20 内容链式更新
     * @apiName chainUpdate
     * @apiGroup Teacher
     *
     * @apiDescription 通过elementId来更新内容(内容之间用、号隔开)
     *
     * @apiParam {String} elementId 要素ID
     * @apiParam {String} contents 内容链(内容之间用、号隔开)
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementId":14,//要素ID
     *     "contents":"学生、家长满意度"//内容链子，用、隔开，注意这个会把数据库里原来对应的要素的内容删除再添加
     * }
     * @apiSuccess (Error 404) {int} msg 没有elementId
     * @apiSuccess (Error 404) {int} msg 没有contents
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {String[]} reason 更新成功
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500) {String[]} reason 错误列表
     */
    public CompletionStage<Result> chainUpdate(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long elementId=(jsonNode.findPath("elementId") instanceof MissingNode ?null: jsonNode.findPath("elementId").asLong());
            List<String> contents=(jsonNode.findPath("contents") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("contents").asText().split("、")).toList());

            if(elementId==null) return ok("没有elementId");
            if(contents==null) return ok("没有contents");

            List<Content> contentList = Content.find.query().where().eq("element_id",elementId).findList();
            try(Transaction transaction=Content.find.db().beginTransaction()){
                DB.deleteAll(contentList);
                transaction.commit();
            }catch (Exception e){
                return okCustomNode(false,List.of("删除旧内容出错: "+e));
            }

            List<Content> addContentList=new ArrayList<>();
            contents.forEach(name->{
                Content content = new Content();
                content.setElementId(elementId);
                content.setContent(name);
                addContentList.add(content);
            });
            try(Transaction transaction=Content.find.db().beginTransaction()){
                DB.saveAll(addContentList);
                transaction.commit();
            }catch (Exception e){
                return okCustomNode(false,List.of("添加新内容出错: "+e));
            }
            return okCustomNode(true,List.of("更新成功"));

        });
    }

    //获取单个KPI post /v1/tk/kpi/get/
    public CompletionStage<Result> getKPI(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());

            if(id==null) return ok("ID为空");

            KPI kpi = KPI.find.query().where().eq("id",id).setMaxRows(1).findOne();

            return ok(Json.toJson(kpi));
        });
    }

    //添加指标 post /v1/tk/indicator/add/
    public CompletionStage<Result> addIndicator(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Indicator> indicatorList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            if(indicatorList.isEmpty()) return ok("indicatorList为空");

            try(Transaction transaction=Indicator.find.db().beginTransaction()){
                DB.saveAll(indicatorList);
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(500,e.toString());
            }
            return okCustomJson(200,"添加成功");
        });
    }

    //添加要素 post /v1/tk/element/add/
    public CompletionStage<Result> addElement(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Element> elementList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            if(elementList.isEmpty()) return ok("elementList为空");

            try(Transaction transaction=Element.find.db().beginTransaction()){
                DB.saveAll(elementList);
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(500,e.toString());
            }
            return okCustomJson(200,"添加成功");
        });
    }

    //添加内容 post /v1/tk/content/add/
    public CompletionStage<Result> addContent(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Content> contentList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            if(contentList.isEmpty()) return ok("contentList为空");

            try(Transaction transaction=Content.find.db().beginTransaction()){
                DB.saveAll(contentList);
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(500,e.toString());
            }
            return okCustomJson(200,"添加成功");
        });
    }

    //获取所有该kpiId里未下发的教师
    public CompletionStage<Result> getTeacherListNotDispatch(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());

            if(kpiId==null||kpiId<=0) return okCustomJson(40001,"没有kpiId");

            Role role = Role.find.query().where()
                    .or()
                    .eq("nick_name","老师")
                    .eq("nick_name","教师")
                    .endOr().setMaxRows(1).findOne();
            if(role==null) return okCustomJson(40001,"没有发现教师角色");
            List<User> teacherList = User.find.query().where()
                    .eq("role_id",role.getId())
                    .or()
                        .isNull("dispatch_ids")
                        .not(Expr.like("dispatchIds", "%+"+kpiId+"+%"))
                    .endOr()
                    .findList();

            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("data",Json.toJson(teacherList));
            return ok(node);
        });
    }

    //获取所有已下发的教师(可兼容kpiId内未下发)
    public CompletionStage<Result> getTeacherListDispatch(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());

            Role role = Role.find.query().where()
                    .or()
                    .eq("nick_name","老师")
                    .eq("nick_name","教师")
                    .endOr().setMaxRows(1).findOne();
            if(role==null) return okCustomJson(CODE40001,"没有发现教师角色");

            ExpressionList<User> teacherExpressionList=User.find.query().where()
                    .eq("role_id",role.getId())
                    .isNotNull("dispatch_ids");
            if(kpiId!=null) teacherExpressionList.icontains("dispatch_ids","+"+kpiId+"+");
            List<User> teacherList =teacherExpressionList
                    .findList();

            List<String> kpiIds = teacherList.stream()
                    .map(User::getDispatchIds)
                    .toList();
            List<KPI> kpiList = KPI.find.query().where()
                    .in("id", kpiIds)
                    .findList();
            List<String> kpiNames=new ArrayList<>();
            teacherList.forEach(teacher->{
                String dispatchIdsString = teacher.getDispatchIds()
                        .replace("++", ",").replace("+", "");
                List<String> dispatchIds = Arrays.stream(dispatchIdsString.split(",")).toList();
                String kpiName = kpiList.stream()
                        .filter(v1 -> dispatchIds.contains(String.valueOf(v1.getId())))
                        .map(KPI::getTitle)
                        .collect(Collectors.joining(","));
                kpiNames.add(kpiName);
            });

            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("data",Json.toJson(teacherList));
            node.set("kpiNames",Json.toJson(kpiNames));
            return ok(node);
        });
    }

    //获取所有教师
    public CompletionStage<Result> getTeacherList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Role role = Role.find.query().where()
                    .or()
                    .eq("nick_name","老师")
                    .eq("nick_name","教师")
                    .endOr().setMaxRows(1).findOne();
            if(role==null) return okCustomJson(40001,"没有发现教师角色");

            List<User> userList=User.find.query().where()
                    .eq("role_id",role.getId())
                    .findList();

            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list",Json.toJson(userList));
            return ok(node);
        });
    }

    //添加教师
    public CompletionStage<Result> addTeacher(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            User user=objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            Role role = Role.find.query().where()
                    .or()
                    .eq("nick_name","老师")
                    .eq("nick_name","教师")
                    .endOr().setMaxRows(1).findOne();
            if(role==null) return okCustomJson(40001,"没有发现教师角色");
            user.setRoleId(role.getId());

            try(Transaction transaction = User.find.db().beginTransaction()){
                user.save();
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(5000,"添加失败: "+e);
            }

            return okCustomJson(200,"添加成功");
        });
    }

    //删除教师
    public CompletionStage<Result> deleteTeacher(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            long id=jsonNode.findPath("id").asLong();

            if(id<=0) return okCustomJson(CODE40001,"没有教师id");
            User user = User.find.query().where()
                    .eq("id", id)
                    .setMaxRows(1).findOne();
            if(user==null) return okCustomJson(CODE40001,"没有该用户");

            try(Transaction transaction = User.find.db().beginTransaction()){
                user.delete();
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(CODE40002,"删除失败: "+e);
            }

            return okCustomJson(CODE200,"删除成功");
        });
    }

    //获取用户信息
    public CompletionStage<Result> getUserList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());
            String phone=(jsonNode.findPath("phone") instanceof MissingNode ?null:jsonNode.findPath("phone").asText());
            String userName=(jsonNode.findPath("userName") instanceof MissingNode ?null:jsonNode.findPath("userName").asText());
            String typeName=(jsonNode.findPath("typeName") instanceof MissingNode ?null:jsonNode.findPath("typeName").asText());
            Long roleId=(jsonNode.findPath("roleId") instanceof MissingNode ?null:jsonNode.findPath("roleId").asLong());

            ExpressionList<User> userExpressionList = User.find.query().where();
            if(id!=null&&id>0) userExpressionList.eq("id",id);
            if(phone!=null&&!phone.isEmpty()) userExpressionList.icontains("phone",phone);
            if(userName!=null&&!userName.isEmpty()) userExpressionList.icontains("user_name",userName);
            if(typeName!=null&&!typeName.isEmpty()) userExpressionList.icontains("type_name",typeName);
            if(roleId!=null&&roleId>0) userExpressionList.eq("role_id",roleId);

            List<User> userList = userExpressionList.findList();
            List<Role> roleList = Role.find.query().where().findList();
            userList.forEach(user->{
                user.setRole(roleList.stream().filter(v1-> Objects.equals(v1.getId(), user.getRoleId())).findFirst().orElse(null));
            });

            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list",Json.toJson(userList));
            return ok(node);
        });
    }

    //获取角色信息
    public CompletionStage<Result> getRoleList(Http.Request request){
        return CompletableFuture.supplyAsync(()->{

            List<Role> roleList = Role.find.query().where().findList();

            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list",Json.toJson(roleList));
            return ok(node);
        });
    }

    //删除用户
    public CompletionStage<Result> deleteUser(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long id=(jsonNode.findPath("id") instanceof MissingNode ?null:jsonNode.findPath("id").asLong());

            if(id==null) return okCustomJson(40001,"没有用户ID");

            User user = User.find.query().where().eq("id", id).setMaxRows(1).findOne();
            try(Transaction transaction = User.find.db().beginTransaction()){
                if(user!=null) DB.delete(user);
                else return okCustomJson(40001,"没有该用户");
                transaction.commit();
            } catch (Exception e) {
                return okCustomJson(500,"删除用户出错: "+e);
            }
            return okCustomJson(200,"删除成功");
        });
    }

    //获取附件信息 taskId
    public CompletionStage<Result> getTeacherFile(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            long taskId=(jsonNode.findPath("taskId") instanceof MissingNode ?0:jsonNode.findPath("taskId").asLong());

            if(taskId<=0) return okCustomJson(CODE40001,"没有任务ID");

            TeacherTask tt = TeacherTask.find.query().where()
                    .eq("id", taskId)
                    .setMaxRows(1).findOne();
            if(tt==null) return okCustomJson(CODE40001,"没有该任务");

            long tesId = tt.getTesId();
            TeacherElementScore tes = TeacherElementScore.find.query().where()
                    .eq("id", tesId)
                    .setMaxRows(1).findOne();
            if(tes==null) return okCustomJson(CODE40001,"没有该要素");

            List<TeacherContentScore> tcsList = TeacherContentScore.find.query().where()
                    .eq("element_id", tes.getElementId())
                    .findList();
            List<TeacherContentScore> tcsHasFileList = tcsList.stream()
                    .filter(v1 -> v1.getFileId() > 0)
                    .toList();

            List<Long> fileIds =tcsHasFileList.stream()
                    .map(TeacherContentScore::getFileId)
                    .toList();
            List<TeacherFile> tfList = TeacherFile.find.query().where()
                    .in("id", fileIds)
                    .findList();

            List<Long> contentIds = tfList.stream()
                    .map(TeacherFile::getContentId)
                    .toList();
            List<Content> contentList = Content.find.query().where()
                    .in("id", contentIds)
                    .findList();

            List<Map<String,Object>> mpList=new ArrayList<>();
            tfList.forEach(tf->{
                Map<String, Object> map = new HashMap<>();
                Content content = contentList.stream()
                        .filter(v1 -> Objects.equals(v1.getId(), tf.getContentId()))
                        .findFirst()
                        .orElse(null);
                map.put("content",content!=null?content.getContent():null);
                map.put("description",tf.getDescription());
                map.put("path",tf.getFilePath());
                mpList.add(map);
            });

            ObjectNode node = Json.newObject();
            node.put(CODE, CODE200);
            node.set("list",Json.toJson(mpList));
            return ok(node);
        });
    }

    public CompletionStage<Result> addTypeJson(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            String jsonText=jsonNode.findPath("jsonText").asText();
            String description=jsonNode.findPath("description").asText();

            if(ValidationUtil.isEmpty(jsonText)) return okCustomJson(CODE40001,"jsonText为空");
            KPIScoreType kpiScoreType = new KPIScoreType();
            kpiScoreType.setDescription(description);
            kpiScoreType.setJsonParam(jsonText);
            try(Transaction transaction = KPIScoreType.find.db().beginTransaction()){
                kpiScoreType.save();
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(CODE40002,"添加出错: "+e);
            }

            return okCustomJson(CODE200,"添加成功");
        });
    }

    public CompletionStage<Result> deleteTypeJson(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            long id=jsonNode.findPath("id").asLong();

            if(id<=0) return okCustomJson(CODE40001,"id为空");
            KPIScoreType kpiScoreType = KPIScoreType.find.byId(id);
            if(kpiScoreType==null) return okCustomJson(CODE40001,"没有该对象");

            try(Transaction transaction = KPIScoreType.find.db().beginTransaction()){
                kpiScoreType.delete();
                transaction.commit();
            }catch (Exception e){
                return okCustomJson(CODE40002,"删除出错: "+e);
            }

            return okCustomJson(CODE200,"删除成功");
        });
    }

    public CompletionStage<Result> getTypeJsonList(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            List<KPIScoreType> all = KPIScoreType.find.all();
            ObjectNode node = Json.newObject();
            node.put("code", 200);
            node.set("list",Json.toJson(all));
            return ok(node);
        });
    }

    //新评分
    /***
     * {
     *     "userId":1,//用户ID
     *     "tcs":[//教师评分对应的内容
     *         {
     *             "contentId":1,//内容ID
     *             "time":1,//次数 (除了count外其他都是 1)
     *             "type":"exclude"
     *             "var":{
     *                 "name":"完成",
     *                 "score":30
     *             }
     *         },
     *         {
     *             "contentId":4,
     *             "time":2,
     *             "type":"count"
     *             "var":{
     *             }
     *         },
     *         {
     *             "contentId":49,
     *             "time":1,
     *             "type":"select",
     *             "var":{
     *                 "name":"一等奖",
     *                 "score":18
     *             }
     *         }
     *     ]
     * }
     */
    public CompletionStage<Result> newGrade(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId= jsonNode.findPath("userId") instanceof MissingNode ? null : jsonNode.findPath("userId").asLong();
            List<Map<String,Object>> teacherContentScores= objectMapper.convertValue(jsonNode.findPath("tcs"), new TypeReference<>() {});

            if(userId==null) return ok("没有userId");
            if(teacherContentScores==null) return ok("tcs为空");

            List<Long> contentIds = teacherContentScores.stream().map(v1 -> Long.parseLong(v1.get("contentId").toString())).toList();
            List<Content> contentList = Content.find.query().where().in("id",contentIds).findList();
            List<Element> elementList=Element.find.query().where().in("id",contentList.stream().map(Content::getElementId).toList()).findList();
            List<TeacherContentScore> tcsList=TeacherContentScore.find.query().where().in("content_id",contentIds).findList();

            List<TeacherContentScore> updateTeacherContentScoreList=new ArrayList<>();
            Transaction transaction = DB.beginTransaction();

            teacherContentScores.forEach(tcsMp->{
                long contentId = Long.parseLong(tcsMp.get("contentId").toString());
                int time = Integer.parseInt(tcsMp.get("time").toString());
                String type = tcsMp.get("type").toString();
                Map<String,Object> varMp= (Map<String, Object>) tcsMp.get("var");

                double score = varMp.get("score")!=null?Double.parseDouble(varMp.get("score").toString()):0.0;

                TeacherContentScore tcs = tcsList.stream().filter(v1 -> v1.getContentId() == contentId).findFirst().orElse(null);
                Content content = contentList.stream().filter(v1 -> v1.getId() == contentId).findFirst().orElse(null);

                if(tcs!=null&&content!=null){
                    if(type.contains("exclude")){
                        if(score>0.0){
                            tcs.setScore(mxValue);
                        }else{
                            tcs.setScore(-mxValue);
                        }
                        tcs.setTime(time);
                    }
                    else if(type.contains("select")){
                        tcs.setScore(score);
                        tcs.setTime(time);
                    }
                    else{
                        Element element = elementList.stream().filter(v1 -> Objects.equals(v1.getId(), content.getElementId())).findFirst().orElse(null);
                        double countScore=time*content.getScore();
                        if(element!=null){
                            if(countScore>element.getScore()){
                                countScore=element.getScore();
                            }
                        }
                        tcs.setScore(countScore);
                        tcs.setTime(time);
                    }
                    if(tcs.getScore()==null){
                        tcs.setScore(0.0);
                    }
                    updateTeacherContentScoreList.add(tcs);
                }
            });
            try{
                DB.updateAll(updateTeacherContentScoreList);
            } catch (Exception e) {
                transaction.rollback();
                return okCustomJson(CODE40002,"内容分数更新失败");
            }
            Set<Long> elementIds = contentList.stream().map(models.school.kpi.v3.Content::getElementId).collect(Collectors.toSet());
            List<TeacherElementScore> teacherElementScoreList = TeacherElementScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherElementScore> filterTeacherElementScoreList = teacherElementScoreList.stream().filter(v1 -> elementIds.contains(v1.getElementId())).toList();
            filterTeacherElementScoreList.forEach(tes->{
                tes.setScore(updateTeacherContentScoreList.stream().filter(v1-> Objects.equals(v1.getElementId(), tes.getElementId())).map(TeacherContentScore::getScore).mapToDouble(Double::doubleValue).sum());
            });
            try{
                DB.updateAll(filterTeacherElementScoreList);
            } catch (Exception e) {
                transaction.rollback();
                return okCustomJson(CODE40002,"要素分数更新失败");
            }

            Set<Long> indicatorIds = elementList.stream().map(Element::getIndicatorId).collect(Collectors.toSet());
            List<TeacherIndicatorScore> teacherIndicatorScoreList=TeacherIndicatorScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherIndicatorScore> filterTeacherIndicatorScoreList = teacherIndicatorScoreList.stream().filter(v1 -> indicatorIds.contains(v1.getIndicatorId())).toList();
            List<TeacherElementScore> list1 = teacherElementScoreList.stream().filter(v1 ->indicatorIds.contains(v1.getIndicatorId())).toList();
            filterTeacherIndicatorScoreList.forEach(tis->{
                double sum = list1.stream().filter(v1 -> v1.getScore() != null && Objects.equals(v1.getIndicatorId(), tis.getIndicatorId())).map(TeacherElementScore::getScore).mapToDouble(Double::doubleValue).sum();
                if(sum>=Math.abs(mxValue)||sum<=-Math.abs(mxValue)){
                    double tmpSum=sum;
                    sum=sum-((int)sum/mxValue)*mxValue;
                    if(sum==0.0){
                        sum=tmpSum;
                    }
                }
                tis.setScore(sum);
            });
            try{
                DB.updateAll(filterTeacherIndicatorScoreList);
            } catch (Exception e) {
                transaction.rollback();
                return okCustomJson(CODE40002,"指标更新失败");
            }

            List<Long> list = teacherIndicatorScoreList.stream().map(TeacherIndicatorScore::getKpiId).toList();
            TeacherKPIScore tks = TeacherKPIScore.find.query().where().eq("user_id", userId).in("kpi_id", list).setMaxRows(1).findOne();
            if(tks!=null){
                double sum = teacherIndicatorScoreList.stream().map(TeacherIndicatorScore::getScore).filter(v1-> Objects.nonNull(v1) && v1 < mxValue && v1 > -mxValue).mapToDouble(Double::doubleValue).sum();
                tks.setScore(sum);
                try{
                    tks.update();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    return okCustomJson(CODE40002,"kpi分数更新失败");
                }
            }else{
                return okCustomJson(CODE40002,"该教师无KPI");
            }
            return okCustomJson(CODE200,"评分成功");
        });
    }

    //==========================================
    //工具
    private void testPDF() throws IOException {
        byte[] pdfBytes = AssessmentPDF.v3ExportToPdf(1L,1L);

        this.storeFile(pdfBytes,"E:/test","test.pdf");
    }

    private void storeFile(byte[] data, String directory, String filename) throws IOException {
        Path dir = Paths.get(directory);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        Path file = dir.resolve(filename);
        Files.write(file, data);
        System.out.println("存储完成");
    }

    private JsonNode createSuccessResponse(ParseResult result) {
        return objectMapper.createObjectNode()
                .put("success", true)
                .put("message", result.getMessage())
                .put("fileName", result.getFileName())
                .put("fileType", result.getFileType())
                .put("totalTables", result.getTotalTables())
                .set("tables", objectMapper.valueToTree(result.getTables()));
    }

    private JsonNode createErrorResponse(String message) {
        return objectMapper.createObjectNode()
                .put("success", false)
                .put("message", message);
    }

    private static List<Object> filterEqualTo(List<Object> list, double target) {
        final double epsilon = 1e-10; // 精度容差

        return list.stream()
                .filter(obj -> {
                    Optional<Double> value = convertToDouble(obj);
                    return value.isPresent() && Math.abs(value.get() - target) < epsilon;
                })
                .collect(Collectors.toList());
    }

    private static Optional<Double> convertToDouble(Object obj) {
        if (obj == null) {
            return Optional.empty();
        }

        try {
            if (obj instanceof Number) {
                return Optional.of(((Number) obj).doubleValue());
            } else if (obj instanceof String) {
                return Optional.of(Double.parseDouble((String) obj));
            } else {
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private JsonNode buildKPIData(PagedList<KPI> pagedList) {
        List<Object> dataList = new ArrayList<>();

        for (KPI kpi : pagedList.getList()) {
            ObjectNode kpiNode = Json.newObject();
            // 显式设置每个字段，避免序列化问题
            kpiNode.put("id", kpi.getId());
            kpiNode.put("title", kpi.getTitle());
            // 添加其他需要的字段...

            dataList.add(kpiNode);
        }

        return Json.toJson(dataList);
    }

    /**
     * 方法1: 最简单直接的转换
     */
    private Map<String, Object> simpleConvertVar(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }

        try {
            Map<String, Object> map = new HashMap<>();

            java.lang.reflect.Field nameField = obj.getClass().getDeclaredField("name");
            java.lang.reflect.Field scoreField = obj.getClass().getDeclaredField("score");

            nameField.setAccessible(true);
            scoreField.setAccessible(true);

            map.put("name", nameField.get(obj));
            map.put("score", scoreField.get(obj));

            return map;

        } catch (Exception e) {
            // 如果反射失败，返回空Map
            return new HashMap<>();
        }
    }
}
