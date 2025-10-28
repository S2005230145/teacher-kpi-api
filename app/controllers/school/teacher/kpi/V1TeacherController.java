package controllers.school.teacher.kpi;

import akka.remote.artery.aeron.TaskRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import controllers.BaseAdminSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import jakarta.inject.Inject;
import models.school.kpi.param.AddEvaluationParam;
import models.school.kpi.param.TPAParam;
import models.school.kpi.v1.KPI;
import models.school.kpi.v1.indicator.EvaluationContent;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.V1TeacherRepository;
import utils.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class V1TeacherController extends BaseAdminSecurityController {

    @Inject
    V1TeacherRepository v1TeacherRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    //查询KPI信息
    //请求格式
//    {
//        "isPage":true,
//            "userId":1,
//            "startTime":"2025-10-10 11:11:11",
//            "endTime":"2026-10-10 11:11:11"
//    }
    public CompletionStage<Result> getList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            boolean isPage=(!(jsonNode.findPath("isPage") instanceof MissingNode) && jsonNode.findPath("isPage").asBoolean());
            long userId=(jsonNode.findPath("userId") instanceof MissingNode ?0:jsonNode.findPath("userId").asLong());
            LocalDate startTime=(jsonNode.findPath("startTime") instanceof MissingNode ?null: LocalDate.parse(jsonNode.findPath("startTime").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            LocalDate endTime=(jsonNode.findPath("endTime") instanceof MissingNode ?null: LocalDate.parse(jsonNode.findPath("endTime").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            ExpressionList<KPI> expressionListKPI = KPI.find.query().where();
            if(userId!=0) expressionListKPI.eq("user_id",userId);
            if(startTime!=null) expressionListKPI.ge("start_time",startTime);
            if(endTime!=null) expressionListKPI.le("end_time",endTime);

            Pair<List<KPI>, PagedList<KPI>> kpiList = v1TeacherRepository.getKPIList(expressionListKPI,1000);
            ObjectNode result = Json.newObject();
            if(isPage){
                PagedList<KPI> second = kpiList.second();

                int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?0:jsonNode.findPath("currentPage").asInt());
                int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?0:jsonNode.findPath("pageSize").asInt());

                result.set("data", Json.toJson(second.getList()));
                result.put("currentPage", currentPage<=0?1:currentPage);
                result.put("pageSize", pageSize<=0?10:pageSize);
                result.put("totalCount", second.getTotalCount());
            }else{
                result.set("data", Json.toJson(kpiList.first()));
            }
            return ok(result);
        });
    }

    //添加评价指标与评价要素
    //请求格式
//    [
//    {
//        "indicatorName":"test1",
//            "indicatorSubName":"test2",
//            "kpiId":1,
//            "tpalist":[
//        {
//            "evaluationElementList":[
//            {"name":"zs"},
//            {"name":"ls"}
//                ],
//            "evaluationContentList":[
//            {"evaluationContent":"lakajk","score":10.0},
//            {"evaluationContent":"jnkasda","score":20.0}
//                ],
//            "evaluationStandard":"ssss",
//                "score":100.0
//        }
//        ]
//    }
//]
    public CompletionStage<Result> addAll(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            try {
                List<AddEvaluationParam> addEvaluationParamList = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

                List<Pair<Boolean, List<String>>> booleanListPairs=new ArrayList<>();
                addEvaluationParamList.forEach(addEvaluationParam->{
                    booleanListPairs.add(v1TeacherRepository.addAll(addEvaluationParam));
                });

                ObjectNode result = Json.newObject();
                Boolean status=booleanListPairs.stream().allMatch(Pair::first);
                result.put("status",status);
                result.put("msg",status?"添加成功":"添加失败");
                result.set("data",Json.toJson(booleanListPairs));
                return ok(result);
            } catch (Exception e) {
                System.out.println("出错: "+e);
                return internalServerError();
            }
        });
    }

    //删除评价信息
//    {
//        "ids":"1,2,3",
//        "type":1
//    }
    public CompletionStage<Result> deleteAll(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            List<Long> ids=(jsonNode.findPath("ids") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("ids").asText().split(",")).map(String::trim).map(Long::valueOf).collect(Collectors.toList()));
            Integer type=(jsonNode.findPath("type") instanceof MissingNode ?null:jsonNode.findPath("type").asInt());

            if(ids==null) return ok("ids为空");
            if(type==null) return ok("type为空");

            ObjectNode result = Json.newObject();
            Pair<Boolean, List<String>> booleanPair = switch (type) {
                case 1 -> v1TeacherRepository.deleteKPI(ids);
                case 2 -> v1TeacherRepository.deleteIndicator(ids);
                default -> v1TeacherRepository.deleteTPA(ids);
            };
            result.put("status",booleanPair.first());
            result.put("msg",booleanPair.first()?"删除成功":"删除失败");
            result.set("data",Json.toJson(booleanPair.second()));
            return ok(result);
        });
    }

    //评分
//    {
//        "userId":1,
//        "kpiId":1,
//        "evaluationContentList":[{"evaluationContent":"zs","score":100.0}]
//    }
    public CompletionStage<Result> audit(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());
            List<EvaluationContent> evaluationContentList= objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            if(userId==null) return ok("userId为空");
            if(kpiId==null) return ok("kpiId为空");
            if(evaluationContentList==null) return ok("EvaluationContent为空");
            Pair<Boolean, List<String>> booleanPair = v1TeacherRepository.audit(userId, kpiId, evaluationContentList);

            ObjectNode result = Json.newObject();
            result.put("status",booleanPair.first());
            result.put("msg",booleanPair.first()?"评分成功":"评分失败");
            result.set("data",Json.toJson(booleanPair.second()));
            return ok(result);
        });
    }

    //绩效考核下发
    //{
    //     "userId":10,
    //     "ids":"1,2,4,6,3",
    //     "kpiId":1
    // }
    public CompletionStage<Result> taskAssessment(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            List<Long> ids=(jsonNode.findPath("ids") instanceof MissingNode ?null: Arrays.stream(jsonNode.findPath("ids").asText().split(",")).map(String::trim).map(Long::valueOf).collect(Collectors.toList()));
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());

            if(userId==null) return ok("userId为空");
            if(kpiId==null) return ok("kpiId为空");
            if(ids==null) return ok("ids为空");
            Pair<Boolean, List<String>> booleanListPair = v1TeacherRepository.addTask(userId,ids ,kpiId);
            ObjectNode result = Json.newObject();
            result.put("status",booleanListPair.first());
            result.put("msg",booleanListPair.first()?"评分成功":"评分失败");
            result.set("data",Json.toJson(booleanListPair.second()));
            return ok(result);
        });
    }

    public CompletionStage<Result> export(){
        return CompletableFuture.supplyAsync(()->{
            return ok();
        });
    }
}
