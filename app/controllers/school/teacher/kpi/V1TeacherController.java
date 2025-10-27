package controllers.school.teacher.kpi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.BaseAdminSecurityController;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import jakarta.inject.Inject;
import models.school.kpi.v1.KPI;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.V1TeacherRepository;
import utils.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class V1TeacherController extends BaseAdminSecurityController {

    @Inject
    V1TeacherRepository v1TeacherRepository;

    //查询信息
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

            Pair<List<KPI>, PagedList<KPI>> kpiList = v1TeacherRepository.getKPIList(expressionListKPI);
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
    public CompletionStage<Result> addAll(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{


            return ok();
        });
    }
}
