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
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.V3TeacherRepository;
import utils.AssessmentPDF;
import utils.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class V3TeacherController  extends BaseAdminSecurityController {
    @Inject
    V3TeacherRepository v3TeacherRepository;

    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * @api {POST} /v1/tk/kpi/add/  01 kpi添加
     * @apiName addKpi
     * @apiGroup User
     *
     * @apiDescription 批量创建kpi
     *
     * @apiParam {String} title KPI标题
     *
     * @apiParamExample {json} 请求示例:
     *     [
     *       {
     *         "title": ""
     *       }
     *     ]
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @apiGroup User
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
     *         "indicatorName":"",
     *         "subName":"",
     *         "kpiId":1,
     *         "elementList":[
     *             {
     *                 "element":null,
     *                 "criteria":"",
     *                 "contentList":[
     *                     {
     *                         "content":"12、123、12314"
     *                     }
     *                 ]
     *             }
     *         ]
     *       }
     *     ]
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/getList/  03 获取所有指标
     * @apiName getList
     * @apiGroup User
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
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/getKpiList/  04 获取所有KPI
     * @apiName getKpiList
     * @apiGroup User
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
     *         "pageSize":10
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/getElementList/  05 获取所有Element
     * @apiName getElementList
     * @apiGroup User
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
     *         "pageSize":10
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/getContentList/  06 获取所有Content
     * @apiName getContentList
     * @apiGroup User
     *
     * @apiDescription 获取所有Content的信息，也可获得对应用户的内容，也可以获取对应要素需要的内容
     *
     * @apiParam {int} currentPage 当前页面
     * @apiParam {int} pageSize 显示条数
     * @apiParam {Long} userId (可选)用户ID
     * @apiParam {Long} elementId (可选)要素ID
     *
     * @apiParamExample {json} 请求示例:
     *       {
     *         "currentPage":1,
     *         "pageSize":10
     *       }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){Object[]} data 数据
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/dispatch/  07 考核规则下发
     * @apiName dispatch
     * @apiGroup User
     *
     * @apiDescription 下发考核给相应教师
     *
     * @apiParam {String} ids 接收的教师id，用,分割
     * @apiParam {Long} kpiId 下发的考核KPI的ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "ids":"",
     *     "kpiId":
     * }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/grade/  08 评分
     * @apiName grade
     * @apiGroup User
     *
     * @apiDescription 教师评分
     *
     * @apiParam {Long} userId 教师ID
     * @apiParam {Long} contentId 内容ID
     * @apiParam {Double} score 分数
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,
     *     "tcs":[
     *         {
     *             "contentId":1,
     *             "score":1.0
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
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason 错误列表
     */
    public CompletionStage<Result> grade(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId= jsonNode.findPath("userId") instanceof MissingNode ? null : jsonNode.findPath("userId").asLong();
            List<TeacherContentScore> teacherContentScores= objectMapper.convertValue(jsonNode.findPath("tcs"), new TypeReference<>() {});

            Pair<Boolean, List<String>> grade = v3TeacherRepository.grade(userId, teacherContentScores);

            return okCustomNode(grade.first(),grade.second());
        });
    }

    /**
     * @api {POST} /v1/tk/export/  09 导出
     * @apiName export
     * @apiGroup User
     *
     * @apiDescription 导出相应教师的KPI的PDF
     *
     * @apiParam {Long} userId 教师ID
     * @apiParam {Long} kpiId KPI的ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1,
     *     "kpiId":1
     * }
     *
     * @apiSuccess (Success 200){File} file PDF文件
     */
    public CompletionStage<Result> export(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId= jsonNode.findPath("userId") instanceof MissingNode ? null : jsonNode.findPath("userId").asLong();
            Long kpiId= jsonNode.findPath("kpiId") instanceof MissingNode ? null : jsonNode.findPath("kpiId").asLong();

            if(userId==null) return ok("userId为空");
            if(kpiId==null) return ok("kpiId为空");

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
     * @api {POST} /v1/tk/export/  10 批量设置要素为系统计算
     * @apiName isAutoCalculator
     * @apiGroup User
     *
     * @apiDescription 通过要素ID批量设置要素为系统自动计算
     *
     * @apiParam {String} elementIds 要素ID集合
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementIds":"1,2,3"
     * }
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){int} msg 更新成功
     */
    public CompletionStage<Result> isAutoCalculator(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            List<Long> elementIds=(jsonNode.findPath("elementIds") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("elementIds").asText().split(",")).map(String::trim).map(Long::valueOf).toList());

            List<Element> elementList=Element.find.query().where().in("id",elementIds).findList();
            elementList.forEach(element -> {
                element.setIsAuto(true);
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
     * @api {POST} /v1/tk/delete/  11 多功能删除
     * @apiName deleteElementOrContentOrIndicatorOrKpi
     * @apiGroup User
     *
     * @apiDescription 可以删除element,content,indicator,kpi 注意越高等级的会连带将低等级的删除
     *
     * @apiParam {String} elementIds (可选)要素删除
     * @apiParam {String} contentIds (可选)内容删除
     * @apiParam {String} indicatorIds (可选)要素删除
     * @apiParam {String} kpiIds (可选)kpi删除
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementIds":"1,2,3",
     *     "contentIds":"1",
     * }
     * @apiSuccess (null) {String} msg 没有需要删除的id
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){String[]} reason 错误列表
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
     * @api {POST} /v1/tk/withDraw/  12 撤销下发
     * @apiName withDraw
     * @apiGroup User
     *
     * @apiDescription 撤销教师的评级
     *
     * @apiParam {String} teacherIds
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "teacherIds":"1,2,3"
     * }
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200){msg} reason 错误列表
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
}
