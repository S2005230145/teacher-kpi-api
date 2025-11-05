package controllers.school.teacher.kpi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseAdminSecurityController;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Inject;
import models.school.kpi.v3.*;
import models.table.ParseResult;
import models.user.Role;
import models.user.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.V3TeacherRepository;
import service.FileParseService;
import utils.AssessmentPDF;
import utils.Pair;
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
     *                                 "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行"
     *                             },
     *                             {
     *                                 "id": 2,
     *                                 "elementId": 1,
     *                                 "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为"
     *                             },
     *                             {
     *                                 "id": 3,
     *                                 "elementId": 1,
     *                                 "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为"
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

            ExpressionList<Indicator> expressionList = Indicator.find.query().where();
            if(userId!=null){
                Set<Long> kpiIds = TeacherKPIScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherKPIScore::getKpiId).collect(Collectors.toSet());
                expressionList.in("kpi_id",kpiIds);
            }

            Pair<PagedList<Indicator>, List<String>> list = v3TeacherRepository.getList(expressionList,currentPage,pageSize);

            return okCustomNode(!list.first().getList().isEmpty(),list.second(),list.first());
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
     *                                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行"
     *                                     },
     *                                     {
     *                                         "id": 2,
     *                                         "elementId": 1,
     *                                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为"
     *                                     },
     *                                     {
     *                                         "id": 3,
     *                                         "elementId": 1,
     *                                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为"
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
    public CompletionStage<Result> getKpiList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            ExpressionList<KPI> expressionList = KPI.find.query().where();
            if(userId!=null){
                Set<Long> kpiIds = TeacherKPIScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherKPIScore::getKpiId).collect(Collectors.toSet());
                expressionList.in("id",kpiIds);
            }
            Pair<PagedList<KPI>, List<String>> list = v3TeacherRepository.getKpiList(expressionList,currentPage,pageSize);

            return okCustomNode(!list.first().getList().isEmpty(),list.second(),list.first());
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
     *                                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行"
     *                                     },
     *                                     {
     *                                         "id": 2,
     *                                         "elementId": 1,
     *                                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为"
     *                                     },
     *                                     {
     *                                         "id": 3,
     *                                         "elementId": 1,
     *                                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为"
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

            ExpressionList<Element> expressionList = Element.find.query().where();
            if(userId!=null){
                Set<Long> elementIds = TeacherElementScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherElementScore::getElementId).collect(Collectors.toSet());
                expressionList.in("id",elementIds);
            }
            Pair<PagedList<Element>, List<String>> list = v3TeacherRepository.getElementList(expressionList,currentPage,pageSize);

            return okCustomNode(!list.first().getList().isEmpty(),list.second(),list.first());
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
     *                         "content": "1、按照教育部《新时代中小学教师职业行为十项准则》、《新时代幼儿园教师职业行为十项准则》执行"//内容名称
     *                     },
     *                     {
     *                         "id": 2,
     *                         "elementId": 1,
     *                         "content": "2.未存在教育部《中小学教师违反职业道德行为处理办法（2018年修订）》、《幼儿园教师违反职业道德行为处理办法》中应予处理的教师违反职业道德的行为"
     *                     },
     *                     {
     *                         "id": 3,
     *                         "elementId": 1,
     *                         "content": "3.未存在《福州市中小学（幼儿园）教师职业行为负面清单》及《福州市教师职业行为负面清单处理办法》中的师德失范行为"
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

            ExpressionList<Content> expressionList = Content.find.query().where();
            if(userId!=null){
                Set<Long> contentIds = TeacherContentScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherContentScore::getElementId).collect(Collectors.toSet());
                expressionList.in("id",contentIds);
            }
            if(elementId!=null){
                expressionList.eq("element_id",elementId);
            }
            Pair<PagedList<Content>, List<String>> list = v3TeacherRepository.getContentList(expressionList,currentPage,pageSize);

            return okCustomNode(!list.first().getList().isEmpty(),list.second(),list.first());
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

            Pair<Boolean, List<String>> booleanListPair = v3TeacherRepository.dispatchAll(ids, kpiId);

            return okCustomNode(booleanListPair.first(),booleanListPair.second());
        });
    }

    /**
     * @api {POST} /v1/tk/grade/  09 评分
     * @apiName grade
     * @apiGroup Teacher
     *
     * @apiDescription 教师评分
     *
     * @apiParam {Long} userId 教师ID
     * @apiParam {Long} contentId 内容ID
     * @apiParam {Double} score 分数
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,//用户ID
     *     "tcs":[//教师评分对应的内容
     *         {
     *             "contentId":1,//内容ID
     *             "score":1.0//初始分数
     *         },
     *         {
     *             "contentId":2,
     *             "score":2.0
     *         },
     *         {
     *             "contentId":3,
     *             "score":4.0
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
                        .withHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"")
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
     * @apiParam {String} indicatorIds (可选)要素删除
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
     *     "teacherIds":"1,2,3"//撤销的用户ID
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

            if(teacherIds==null) return ok("没有教师ID");

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
     *     "LeaderIds":"1,2,3"//发给的领导ID
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

            if(LeaderIds==null) return ok("LeaderIds为空");
            if(userId==null) return ok("userId为空");

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
        });
    }

    /**
     * @api {POST} /v1/tk/get/leader/  15 获取所有领导
     * @apiName withDraw
     * @apiGroup Teacher
     *
     * @apiDescription 获取所有领导领导
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
            Role one = Role.find.query().where().eq("nick_name", "领导").setMaxRows(1).findOne();
            Long roleId = Objects.requireNonNull(one).getId();

            List<User> leadersList = User.find.query().where().eq("role_id", roleId).findList();
            leadersList.forEach(leaders->{
                leaders.setRole(one);
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

            List<TeacherTask> leaderTeacherTaskList = TeacherTask.find.query().where().eq("status", "待完成")
                    .or()
                    .eq("parent_ids", userId)
                    .like("parent_ids", userId + ",%")
                    .like("parent_ids", "%," + userId)
                    .like("parent_ids", "%," + userId + ",%")
                    .endOr().findList();
            List<TeacherElementScore> teslist = TeacherElementScore.find.query().where().in("id", leaderTeacherTaskList.stream().map(TeacherTask::getTesId)).findList();
            List<User> userList = User.find.query().where().in("id", leaderTeacherTaskList.stream().map(TeacherTask::getUserId)).findList();

            leaderTeacherTaskList.forEach(tt->{
                tt.setTeacherElementScore(teslist.stream().filter(tes-> Objects.equals(tt.getTesId(), tes.getId())).findFirst().orElse(null));
                tt.setUserName(Objects.requireNonNull(userList.stream().filter(user -> Objects.equals(tt.getUserId(), user.getId())).findFirst().orElse(null)).getUserName());
            });

            return okCustomNode(true,null,leaderTeacherTaskList);
        });
    }

    /**
     * @api {POST} /v1/tk/add/leader/score/  17 上级评分
     * @apiName addLeaderScore
     * @apiGroup Teacher
     *
     * @apiDescription 该上级用户的评分，用于最终评分
     *
     * @apiParam {Long} userId 用户ID
     * @apiParam {Long} tesId 要素ID
     * @apiParam {Double} score 分数
     * @apiParamExample {json} 请求示例:
     * {
     *      "userId":1,//上级用户ID
     *      "data":[
     *          {
     *              "tesId":1,//该教师的评测要素ID
     *              "score":12.0//最终得分
     *          }
     *      ]
     * }
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){msg} reason []
     *
     * @apiSuccess (Error 500){int} code 500
     * @apiSuccess (Error 500){msg} reason 错误信息
     *
     * @apiSuccess (Error 404){int} msg userId为空
     * @apiSuccess (Error 404){int} msg data为空
     */
    public CompletionStage<Result> addLeaderScore(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Map<String,Object> dataMap=objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            if(userId==null) return ok("userId为空");
            if(dataMap==null) return ok("data为空");

            List<TeacherTask> leaderTeacherTaskList = TeacherTask.find.query().where()
                    .or()
                    .eq("parent_ids", userId)
                    .like("parent_ids", userId + ",%")
                    .like("parent_ids", "%," + userId)
                    .like("parent_ids", "%," + userId + ",%")
                    .endOr().findList().stream().filter(tt->tt.getTesId()==Long.parseLong(dataMap.get("tesId").toString())).toList();

            List<String> errorMsg=new ArrayList<>();
            Transaction transaction=DB.beginTransaction();
            List<TeacherElementScore> updateTesList=new ArrayList<>();
            Set<Long> kpiIds=new HashSet<>();
            Set<Long> userIds=new HashSet<>();
            leaderTeacherTaskList.forEach(ltt->{
                ltt.getTeacherElementScore().setFinalScore(Double.parseDouble(dataMap.get("score").toString()));
                ltt.setStatus("已完成");
                updateTesList.add(ltt.getTeacherElementScore());
                kpiIds.add(ltt.getTeacherElementScore().getKpiId());
                userIds.add(ltt.getUserId());
            });

            List<TeacherElementScore> tesList = TeacherElementScore.find.query().where().in("user_id", userIds).in("kpi_id", kpiIds).findList();
            TeacherKPIScore tks = TeacherKPIScore.find.query().where().in("user_id", userIds).in("kpi_id", kpiIds).setMaxRows(1).findOne();
            List<Double> totalScore=new ArrayList<>();
            tesList.forEach(tes->{
                if(tes.getFinalScore()!=null) totalScore.add(tes.getFinalScore());
                else totalScore.add(tes.getScore());
            });

            try{
                DB.updateAll(updateTesList);
                transaction.commit();
            }catch (Exception e){
                errorMsg.add("更新最终得分出错: "+e);
                transaction.rollback();
            }
            if(tks!=null){
                tks.setScore(totalScore.stream().mapToDouble(Double::valueOf).sum());
                try{
                    tks.update();
                    transaction.commit();
                }catch (Exception e){
                    errorMsg.add("更新总分出错: "+e);
                    transaction.rollback();
                }
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
}
