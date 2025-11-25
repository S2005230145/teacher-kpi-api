package controllers.school.teacher.kpi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import controllers.BaseAdminSecurityController;
import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.campus.Campus;
import models.school.kpi.v3.*;
import models.user.User;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.V3TeacherRepository;
import utils.BaseUtil;
import utils.Pair;
import utils.ValidationUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class V3TeacherFrontController extends BaseAdminSecurityController {

    @Inject
    V3TeacherRepository v3TeacherRepository;

    @Inject
    Config config;

    private static String osName = System.getProperty("os.name").toLowerCase();

    //获取该用户对应的绩效参数 userId
    public CompletionStage<Result> getList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            int currentPage=(jsonNode.findPath("currentPage") instanceof MissingNode ?1:jsonNode.findPath("currentPage").asInt());
            int pageSize=(jsonNode.findPath("pageSize") instanceof MissingNode ?10:jsonNode.findPath("pageSize").asInt());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());

            if(userId==null) return ok("userId为空");

            ExpressionList<KPI> expressionList = KPI.find.query().where();
            {
                Set<Long> kpiIds = TeacherKPIScore.find.query().where().eq("user_id",userId).findList().stream().map(TeacherKPIScore::getKpiId).collect(Collectors.toSet());
                expressionList.in("id",kpiIds);
            }

            Pair<PagedList<KPI>, List<String>> list = v3TeacherRepository.getKpiList(expressionList,currentPage,pageSize);

            ObjectNode result = Json.newObject();
            result.set("kpi",Json.toJson(list.first()));

            //===========================================
            List<Map<String,Object>> indicatorMpList=new ArrayList<>();
            List<Long> kpiIds = list.first().getList().stream().map(KPI::getId).toList();
            ExpressionList<TeacherIndicatorScore> tisExpressionList = TeacherIndicatorScore.find.query().where();
            tisExpressionList.eq("user_id", userId);
            List<TeacherIndicatorScore> tisList = tisExpressionList.in("kpi_id", kpiIds).findList();
            if(kpiId!=null){
                tisList=tisList.stream().filter(v1-> Objects.equals(v1.getKpiId(), kpiId)).toList();
            }

            List<Long> tisIds = tisList.stream().map(TeacherIndicatorScore::getIndicatorId).toList();
            ExpressionList<TeacherElementScore> tesExpressionList = TeacherElementScore.find.query().where();
            tesExpressionList.eq("user_id", userId);
            List<TeacherElementScore> tesList = tesExpressionList.in("indicator_id",tisIds).findList();
            List<Long> tesIds = tesList.stream().map(TeacherElementScore::getElementId).toList();

            ExpressionList<TeacherContentScore> tcsExpressionList = TeacherContentScore.find.query().where();
            tcsExpressionList.eq("user_id", userId);
            List<TeacherContentScore> tcsList=tcsExpressionList.in("element_id",tesIds).findList();

            //获取所有KPI
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<TeacherIndicatorScore> finalTisList = tisList;
            list.first().getList().forEach(kpi -> {
                kpi.getIndicatorList().forEach(indicator -> {
                    TeacherIndicatorScore tis = finalTisList.stream().filter(v1 -> Objects.equals(v1.getIndicatorId(), indicator.getId())).findFirst().orElse(null);
                    Map<String, Object> mp = new HashMap<>();
                    mp.put("id",indicator.getId());
                    mp.put("indicatorName",indicator.getIndicatorName()+" "+indicator.getSubName());
                    mp.put("description",null);
                    List<Long> tesIdByInner = tesList.stream().filter(v1 -> Objects.equals(v1.getIndicatorId(), indicator.getId())).map(TeacherElementScore::getElementId).toList();
                    List<TeacherContentScore> tcsListByInner = tcsList.stream().filter(v1 -> tesIdByInner.contains(v1.getElementId())).toList();
                    mp.put("score",tis!=null?tis.getScore():null);
                    double precent= (double)tcsListByInner.stream().filter(v1->v1.getScore()!=null).toList().size()/tcsListByInner.size();
                    int progress = (int) Math.round(precent * 100);
                    mp.put("progress",progress);
                    //TODO 下发时间
                    mp.put("assessTime",LocalDate.now().format(formatter));
                    if(progress==100){
                        mp.put("status","completed");
                        mp.put("statusText","已完成");
                    }else if(progress>0||mp.get("score")!=null){
                        mp.put("status","in-progress");
                        mp.put("statusText","进行中");
                    }else{
                        mp.put("status","pending");
                        mp.put("statusText","待开始");
                    }
                    indicatorMpList.add(mp);
                });
            });
            result.set("indicator",Json.toJson(indicatorMpList));
            return ok(result);
        });
    }

    //获取对应指标的要素信息 indicatorId userId
    public CompletionStage<Result> getElementList(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long indicatorId=(jsonNode.findPath("indicatorId") instanceof MissingNode ?null:jsonNode.findPath("indicatorId").asLong());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(indicatorId==null) return ok("indicatorId为空");
            if(userId==null) return ok("userId为空");

            List<Long> elementIds = TeacherElementScore.find.query().where().eq("user_id", userId).eq("indicator_id", indicatorId).findList().stream().map(TeacherElementScore::getElementId).toList();
            List<Element> elementList=Element.find.query().where().in("id",elementIds).query().findList();
            List<Map<String, Object>> tabs=new ArrayList<>();
            List<List<Map<String, Object>>> contents=new ArrayList<>();
            List<Content> contentList = Content.find.query().where().in("element_id",elementIds).findList();
            List<TeacherContentScore> list = TeacherContentScore.find.query().where().eq("user_id", userId).in("element_id",elementIds).findList();
            elementList.forEach(element->{
                Map<String, Object> tab = new HashMap<>();
                tab.put("id",element.getId());
                tab.put("name",element.getElement());
                tabs.add(tab);
                List<Map<String, Object>> contents1=new ArrayList<>();
                contentList.stream().filter(v1-> Objects.equals(v1.getElementId(), element.getId())).toList().forEach(contentTmp->{
                    Map<String, Object> content = new HashMap<>();
                    content.put("id",contentTmp.getId());
                    content.put("title",contentTmp.getContent());
                    content.put("description",null);
                    content.put("time", Objects.requireNonNull(list.stream().filter(v1 -> Objects.equals(v1.getContentId(), contentTmp.getId())).findFirst().orElse(null)).getTime());
                    contents1.add(content);
                });
                contents.add(contents1);
            });
            ObjectNode result = Json.newObject();
            result.set("tabs",Json.toJson(tabs));
            result.set("contents",Json.toJson(contents));
            return ok(result);
        });
    }

    public CompletionStage<Result> getElementList2(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long indicatorId=(jsonNode.findPath("indicatorId") instanceof MissingNode ?null:jsonNode.findPath("indicatorId").asLong());
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(indicatorId==null) return ok("indicatorId为空");
            if(userId==null) return ok("userId为空");

            List<Long> elementIds = TeacherElementScore.find.query().where().eq("user_id", userId).eq("indicator_id", indicatorId).findList().stream().map(TeacherElementScore::getElementId).toList();
            List<Element> elementList=Element.find.query().where().in("id",elementIds).query().findList();
            List<Map<String, Object>> tabs=new ArrayList<>();
            List<List<Map<String, Object>>> contents=new ArrayList<>();
            List<Content> contentList = Content.find.query().where().in("element_id",elementIds).findList();
            List<TeacherContentScore> list = TeacherContentScore.find.query().where().eq("user_id", userId).in("element_id",elementIds).findList();
            List<Long> typeIds = contentList.stream().map(Content::getTypeId).toList();
            List<KPIScoreType> kpiScoreTypeList = KPIScoreType.find.query().where().in("id",typeIds).findList();

            List<TeacherElementScore> tesList = TeacherElementScore.find.query().where().in("element_id",elementIds).findList();
            elementList.forEach(element->{
                Map<String, Object> tab = new HashMap<>();
                tab.put("id",element.getId());
                tab.put("name",element.getElement());
                TeacherElementScore teacherElementScore = tesList.stream().filter(v1 -> v1.getElementId() == element.getId()).findFirst().orElse(null);
                tab.put("robotScore",teacherElementScore!=null?teacherElementScore.getRobotScore():null);
                tabs.add(tab);
                List<Map<String, Object>> contents1=new ArrayList<>();
                contentList.stream().filter(v1-> Objects.equals(v1.getElementId(), element.getId())).toList().forEach(contentTmp->{
                    Map<String, Object> content = new HashMap<>();
                    content.put("id",contentTmp.getId());
                    content.put("title",contentTmp.getContent());
                    content.put("description",null);
                    content.put("time", Objects.requireNonNull(list.stream().filter(v1 -> Objects.equals(v1.getContentId(), contentTmp.getId())).findFirst().orElse(null)).getTime());
                    KPIScoreType kpiScoreType = kpiScoreTypeList.stream().filter(v1 -> Objects.equals(v1.getId(), contentTmp.getTypeId())).findFirst().orElse(null);
                    content.put("type",kpiScoreType);
                    if(kpiScoreType!=null){
                        content.put("data",Json.toJson(kpiScoreType.getJsonParam()));
                    }
                    contents1.add(content);
                });
                contents.add(contents1);
            });
            ObjectNode result = Json.newObject();
            result.set("tabs",Json.toJson(tabs));
            result.set("contents",Json.toJson(contents));
            return ok(result);
        });
    }

    //kpi基本数据 userId kpiId
    public CompletionStage<Result> getExamSummary(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{

            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());
            Long kpiId=(jsonNode.findPath("kpiId") instanceof MissingNode ?null:jsonNode.findPath("kpiId").asLong());

            if(userId==null) return ok("userId为空");
            if(kpiId==null) return ok("kpiId为空");

            List<TeacherKPIScore> kpiScoreList = TeacherKPIScore.find.query().where().eq("kpi_id", kpiId).findList();
            TeacherKPIScore tks = kpiScoreList.stream().filter(v1-> Objects.equals(v1.getUserId(), userId)).findFirst().orElse(null);

            List<TeacherKPIScore> filterKpiScoreList= new ArrayList<>(kpiScoreList.stream().filter(v1 -> v1.getScore() != null).toList());
            ObjectNode result = Json.newObject();
            result.put("currentScore",tks!=null?tks.getScore():null);
            filterKpiScoreList.sort((v1,v2)-> Double.compare(v2.getScore(),v1.getScore()));

            OptionalInt firstIndex  = IntStream.range(0, filterKpiScoreList.size()).filter(i -> {
                TeacherKPIScore teacherKPIScore = filterKpiScoreList.get(i);
                return userId.equals(teacherKPIScore.getUserId());
            }).findFirst();
            result.put("rank",firstIndex.isPresent()?firstIndex.getAsInt()+1:null);

            List<TeacherContentScore> teacherContentScoreList = TeacherContentScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherContentScore> filterTeacherContentScoreList = teacherContentScoreList.stream().filter(v1 -> v1.getScore() != null).toList();
            result.put("completionRate",(int)Math.round((double)filterTeacherContentScoreList.size()/teacherContentScoreList.size()*100)+"%");
            return ok(result);
        });
    }

    //该用户的排名和分数
    public CompletionStage<Result> getRankAndScore(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(userId==null) return ok("userId为空");

            List<TeacherKPIScore> kpiScoreList = TeacherKPIScore.find.query().where().findList();
            kpiScoreList = kpiScoreList.stream()
                    .filter(v1 -> v1.getScore() != null && Objects.equals(v1.getUserId(), userId))
                    .toList();
            double sum =kpiScoreList
                    .stream()
                    .map(TeacherKPIScore::getScore)
                    .mapToDouble(Double::valueOf).sum();

            ObjectNode result = Json.newObject();
            result.put("score",sum);

            Map<Long, Double> data = kpiScoreList.stream()
                    .collect(Collectors.groupingBy(
                            TeacherKPIScore::getUserId,
                            Collectors.summingDouble(TeacherKPIScore::getScore)
                    ));

            List<Map.Entry<Long, Double>> sortedEntries  = data.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .toList();
            OptionalInt index = IntStream.range(0, sortedEntries.size())
                    .filter(i -> sortedEntries.get(i).getKey().equals(1L))
                    .findFirst();
            if (index.isPresent()) result.put("rank",index.getAsInt()+ 1);
            else result.set("rank",null);

            return ok(result);
        });
    }

    //获取总考核次数，平均分，完成率
    public CompletionStage<Result> getUserStats(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(userId==null) return ok("userId为空");

            List<TeacherKPIScore> tksList = TeacherKPIScore.find.query().where().eq("user_id",userId).findList();
            ObjectNode result = Json.newObject();
            result.put("totalEvaluations",tksList.stream().filter(v1->v1.getScore()!=null).toList().size());
            OptionalDouble average = tksList.stream().filter(v1 -> v1.getScore() != null).toList().stream().map(TeacherKPIScore::getScore).mapToDouble(Double::valueOf).average();
            result.put("averageScore",average.isPresent()?average.getAsDouble():null);
            List<TeacherContentScore> tcsList = TeacherContentScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherContentScore> filterTcsList = tcsList.stream().filter(v1 -> v1.getScore() != null).toList();
            result.put("completionRate",(int)Math.round((double)filterTcsList.size()/tcsList.size()*100));
            result.put("unreadEvaluations",0);
            return ok(result);
        });
    }

    //获取统计信息
    public CompletionStage<Result> getStatistics(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            ObjectNode result = Json.newObject();
            List<TeacherKPIScore> tksList = TeacherKPIScore.find.query().where().findList();
            List<TeacherKPIScore> tksListFilterScore = tksList.stream().filter(v1 -> v1.getScore() != null).toList();
            Map<Long, Double> data = tksListFilterScore.stream()
                    .collect(Collectors.groupingBy(
                            TeacherKPIScore::getUserId,
                            Collectors.summingDouble(TeacherKPIScore::getScore)
                    ));
            OptionalDouble average = data.values().stream().mapToDouble(Double::valueOf).average();
            result.put("averageScore",average.isPresent()?average.getAsDouble():null);
            List<TeacherKPIScore> excellent = tksListFilterScore.stream().filter(v1 -> v1.getScore() >= 80).toList();
            result.put("excellentRate",(int)Math.round((double)excellent.size()/tksListFilterScore.size()*100));
            List<TeacherKPIScore> pass = tksListFilterScore.stream().filter(v1 -> v1.getScore() >= 60).toList();
            result.put("passRate",(int)Math.round((double)pass.size()/tksListFilterScore.size()*100));

            List<Map<String,Object>> scoreDistributionMpList=new ArrayList<>();
            List<String> range=List.of("90-100","80-89","70-79","60-69","60以下");
            range.forEach(r->{
                Map<String,Object> mp=new HashMap<>();
                mp.put("range",r);
                List<Integer> integers = BaseUtil.extractNumbersWithRegex(r);
                Stream<Double> stream = data.values().stream();
                if(r.contains("-")){
                    stream=stream.filter(v1->v1>=integers.get(0)&&v1<=integers.get(1));
                }else if(r.contains("以上")){
                    stream=stream.filter(v1->v1>=integers.get(0));
                }else if(r.contains("以下")){
                    stream=stream.filter(v1->v1<=integers.get(0));
                }
                int count = stream.toList().size();
                mp.put("count",count);
                mp.put("percentage",(int)Math.round((double)count/tksListFilterScore.size()*100));
                scoreDistributionMpList.add(mp);
            });
            result.set("scoreDistribution",Json.toJson(scoreDistributionMpList));

            List<Map.Entry<Long, Double>> sortedEntries  = data.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .toList();
            if(sortedEntries.size()>=5){
                sortedEntries=sortedEntries.subList(0,5);
            }
            List<User> userList = User.find.query().where().in("id", tksListFilterScore.stream().map(TeacherKPIScore::getUserId).toList()).findList();
            List<Map<String,Object>> teacherRankingMpList=new ArrayList<>();
            sortedEntries.forEach(entry->{
                Map<String, Object> mp = new HashMap<>();
                User user = userList.stream().filter(v1 -> Objects.equals(v1.getId(), entry.getKey())).findFirst().orElse(null);
                mp.put("id",entry.getKey());
                if(user!=null){
                    mp.put("name",user.getUserName());
                    mp.put("department",user.getTypeName());
                }
                mp.put("score",entry.getValue());
                mp.put("change",0);
                teacherRankingMpList.add(mp);
            });
            result.set("teacherRanking",Json.toJson(teacherRankingMpList));

            List<Map<String,Object>> itemAnalysisMpList=new ArrayList<>();
            List<KPI> kpiList = KPI.find.query().where().findList();

            kpiList.forEach(kpi -> {
                List<TeacherKPIScore> filterKpiTskList = tksListFilterScore.stream().filter(v1 -> Objects.equals(kpi.getId(), v1.getKpiId())).toList();
                OptionalDouble average1 = filterKpiTskList.stream().map(TeacherKPIScore::getScore).mapToDouble(Double::valueOf).average();
                Map<String,Object> mp=new HashMap<>();
                mp.put("name",kpi.getTitle());
                mp.put("averageScore",average1.isPresent()?average1.getAsDouble():null);
                mp.put("percentage",average1.isPresent()?average1.getAsDouble():null);
                mp.put("trend",0);
                itemAnalysisMpList.add(mp);
            });
            result.set("itemAnalysis",Json.toJson(itemAnalysisMpList));
            return ok(result);
        });
    }

    //获取KPI信息
    /**
     * @api {POST} /v1/front/tk/getKPI/  03 获取KPI
     * @apiName getKPI
     * @apiGroup New
     *
     * @apiDescription 获取该教师的KPI
     *
     * @apiParam {Long} userId 用户ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "userId":1
     * }
     *
     * @apiSuccess (Error 40001) {int} code 40001
     * @apiSuccess (Error 40001) {int} reason 所有空的信息
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {Object[]} mockData 数据
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code":200
     *     "mockData":[
     *          {
     *              "id":0,
     *              "name":"",//kpi名称
     *              "description":"",//描述 null
     *              "type":"",//类型
     *              "typeText":"",//类型描述
     *              "status":"",//状态
     *              "statusText":"",//状态描述
     *              "currentValue":"",//当前分数
     *              "score":"",//分数
     *              "maxScore":"",//最高分数
     *              "progress":"",//进度
     *              "weight":"",//权重
     *              "targetValue":"",//目标分数
     *              "deadline":"",//截至日期
     *              "createTime":"",//创建时间
     *
     *          }
     *     ]
     * }
     *
     */
    public CompletionStage<Result> getKPI(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            Long userId=(jsonNode.findPath("userId") instanceof MissingNode ?null:jsonNode.findPath("userId").asLong());

            if(userId==null) return ok("userId为空");

            List<Map<String,Object>> mockDataMpList=new ArrayList<>();
            List<TeacherKPIScore> tksList = TeacherKPIScore.find.query().where().eq("user_id",userId).findList();
            List<Long> kpiIds = tksList.stream().map(TeacherKPIScore::getKpiId).toList();
            List<TeacherElementScore> tesList = TeacherElementScore.find.query().where().eq("user_id",userId).in("kpi_id",kpiIds).findList();
            List<Long> elementIds = tesList.stream().map(TeacherElementScore::getElementId).toList();
            List<TeacherContentScore> tcsList = TeacherContentScore.find.query().where().eq("user_id",userId).in("element_id",elementIds).findList();

            List<KPI> kpiList = KPI.find.query().where().in("id",tksList.stream().map(TeacherKPIScore::getKpiId).toList()).findList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            kpiList.forEach(kpi->{
                Map<String, Object> mp = new HashMap<>();
                mp.put("id",kpi.getId());
                mp.put("name",kpi.getTitle());
                mp.put("description",null);
                mp.put("type","teaching");
                mp.put("typeText","教学");
                TeacherKPIScore tks = tksList.stream().filter(v1 -> Objects.equals(v1.getKpiId(), kpi.getId())).findFirst().orElse(null);
                if(tks!=null){
                    List<Long> filterTesIds = tesList.stream().filter(v1 -> Objects.equals(v1.getKpiId(), tks.getKpiId())).map(TeacherElementScore::getElementId).toList();
                    List<TeacherContentScore> filterTcsList = tcsList.stream().filter(v1 -> filterTesIds.contains(v1.getElementId())).toList();
                    List<TeacherContentScore> filterTcsListScore = filterTcsList.stream().filter(v1 -> v1.getScore() != null).toList();
                    if(tks.getScore()==null){
                        mp.put("status","pending");
                        mp.put("statusText","未开始");
                    } else{
                        if(filterTcsListScore.size()==filterTcsList.size()){
                            mp.put("status","completed");
                            mp.put("statusText","已完成");
                        }else{
                            mp.put("status","active");
                            mp.put("statusText","进行中");
                        }
                    }
                    mp.put("currentValue",tks.getScore());
                    mp.put("score",tks.getScore());
                    mp.put("maxScore",tks.getScore());
                    mp.put("progress",(int)Math.round((double)filterTcsListScore.size()/filterTcsList.size()*100));
                }
                else {
                    mp.put("status", null);
                    mp.put("statusText", null);
                    mp.put("currentValue",null);
                    mp.put("score",null);
                    mp.put("maxScore",null);
                    mp.put("progress",null);
                }
                mp.put("weight",100);
                mp.put("targetValue","100分");
                mp.put("deadline",kpi.getEndTime());
                mp.put("createTime",kpi.getCreateTime());
                mockDataMpList.add(mp);
            });
            ObjectNode result = Json.newObject();
            result.set("mockData",Json.toJson(mockDataMpList));
            return ok(result);
        });
    }

    //是否有审核任务(kpi判断)
    public CompletionStage<Result> isAudit(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001, "参数错误");
            long userId = jsonNode.findPath("userId").asLong();
            String kpiIdString = jsonNode.findPath("kpiId").asText();

            if(userId<=0) return okCustomJson(CODE40001, "用户ID不正确");
            List<String> kpiIds=new ArrayList<>();
            if(!ValidationUtil.isEmpty(kpiIdString)) kpiIds=Arrays.stream(kpiIdString.split(",")).toList();

            ExpressionList<TeacherTask> expressionList = TeacherTask.find.query().where()
                    .eq("user_id", userId);
            if(!kpiIds.isEmpty()){
                List<TeacherElementScore> tesList = TeacherElementScore.find.query().where()
                        .in("kpi_id", kpiIds)
                        .findList();
                Set<Long> tesIds = tesList.stream()
                        .map(TeacherElementScore::getId)
                        .collect(Collectors.toSet());
                expressionList.in("tes_id",tesIds);
            }
            int taskNum = expressionList.findCount();

            ObjectNode result = Json.newObject();
            result.put("code", 200);
            result.put("status", taskNum > 0);
            return ok(result);
        });
    }

    //获取人数完成请况信息
    public CompletionStage<Result> getPersonStatus(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            List<TeacherKPIScore> totalList = TeacherKPIScore.find.query().where()
                    .findList();
            //总数
            int total=totalList.size();
            //未开始
            int pending=Math.toIntExact(totalList.stream()
                    .filter(v1 -> v1.getScore() == null)
                    .count());
            List<TeacherKPIScore> totalListNotNull = totalList.stream()
                    .filter(v1 -> v1.getScore() != null)
                    .toList();

            List<Long> userIds = totalListNotNull.stream().map(TeacherKPIScore::getUserId).toList();
            List<TeacherContentScore> tcsList = TeacherContentScore.find.query().where()
                    .in("user_id", userIds)
                    .findList();
            Map<Long,List<TeacherContentScore>> groupedByUserId=tcsList.stream()
                    .collect(Collectors.groupingBy(TeacherContentScore::getUserId));
            Map<Long, Long> nullScoreCounts = groupedByUserId.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    .filter(userScore -> userScore.getScore() == null)
                                    .count()
                    ));
            //进行中
            AtomicInteger progress= new AtomicInteger();
            //已完成
            AtomicInteger completed= new AtomicInteger();
            nullScoreCounts.forEach((userId, count) -> {
                if (count > 0) progress.getAndIncrement();
                else completed.getAndIncrement();
            });

            ObjectNode result = Json.newObject();
            result.put(CODE,CODE200);
            result.put("total",total);
            result.put("pending",pending);
            result.put("completed",completed.get());
            result.put("progress",progress.get());
            return ok(result);
        });
    }

    //上传附件
    public CompletionStage<Result> uploadFile(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            Http.MultipartFormData<play.libs.Files.TemporaryFile> multipartFormData = request.body().asMultipartFormData();
            List<Http.MultipartFormData.FilePart<play.libs.Files.TemporaryFile>> files = multipartFormData.getFiles();

            String description = multipartFormData.asFormUrlEncoded().containsKey("description")?multipartFormData.asFormUrlEncoded().get("description")[0]:null;
            long contentId = multipartFormData.asFormUrlEncoded().containsKey("contentId")?Long.parseLong(multipartFormData.asFormUrlEncoded().get("contentId")[0]):0;
            long userId = multipartFormData.asFormUrlEncoded().containsKey("userId")?Long.parseLong(multipartFormData.asFormUrlEncoded().get("userId")[0]):0;

            if(contentId<=0) return okCustomJson(CODE40001,"没有contentId");
            if(userId<=0) return okCustomJson(CODE40001,"没有userId");

            Http.MultipartFormData.FilePart<Files.TemporaryFile> filePart = files.get(0);
            //存文件
            String filename = filePart.getFilename();
            String suffix = filename.substring(filename.lastIndexOf("."));
            filename=UUID.randomUUID().toString().replace("-", "").toUpperCase()+suffix;
            String path = null;
            if (osName.contains("win")) {
                path=config.getString("fileUpload.windows");
            } else {
                path=config.getString("fileUpload.linux");
            }
            File dir = new File(path, "/teacher_file");
            if (!dir.exists()) dir.mkdirs();
            File destination = new File(dir, filename);
            filePart.getRef().copyTo(destination.toPath(), false);

            TeacherFile teacherFile = new TeacherFile();
            teacherFile.setFilePath("teacher_file/"+filename);
            teacherFile.setDescription(description);
            teacherFile.setContentId(contentId);
            Transaction transaction = DB.beginTransaction();
            try{
                teacherFile.save();
            }catch (Exception e){
                transaction.rollback();
                return okCustomJson(CODE40002,"教师文件出错: "+e);
            }

            TeacherContentScore tcs = TeacherContentScore.find.query().where()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .setMaxRows(1).findOne();
            if(tcs==null) return okCustomJson(CODE40001,"该教师没有被下发的内容");
            tcs.setFileId(teacherFile.getId());
            try{
                tcs.update();
            }catch (Exception e){
                transaction.rollback();
                return okCustomJson(CODE40002,"教师内容更新出错: "+e);
            }
            transaction.commit();
            return okCustomJson(CODE200,"上传成功");
        });
    }

    //是否提交过审核判断
    /**
     * @api {POST} /v1/front/tk/is/submit/audit/  01 审核判断
     * @apiName isSubmitAudit
     * @apiGroup New
     *
     * @apiDescription 是否提交过审核判断
     *
     * @apiParam {Long} elementId 要素ID
     * @apiParam {String} userId 用户ID
     *
     * @apiParamExample {json} 请求示例:
     * {
     *     "elementId":1
     *     "userId":1
     * }
     *
     * @apiSuccess (Error 40001) {int} code 40001
     * @apiSuccess (Error 40001) {int} reason 所有空的信息
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {boolean} status 状态
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code":200
     *     "status":true
     * }
     *
     */
    public CompletionStage<Result> isSubmitAudit(Http.Request request){
        JsonNode jsonNode = request.body().asJson();
        return CompletableFuture.supplyAsync(()->{
            if(jsonNode==null) return okCustomJson(CODE40001,"参数错误");
            long elementId=jsonNode.findPath("elementId").asLong();
            long userId=jsonNode.findPath("userId").asLong();

            if(elementId<=0) return okCustomJson(CODE40001,"elementId为空");
            if(userId<=0) return okCustomJson(CODE40001,"userId为空");

            TeacherElementScore tes = TeacherElementScore.find.query().where().eq("element_id", elementId).eq("user_id", userId).setMaxRows(1).findOne();
            if(tes==null) return okCustomJson(CODE40001,"该用户没有该要素任务");
            TeacherTask tt = TeacherTask.find.query().where().eq("tes_id",tes.getId()).eq("user_id",userId).setMaxRows(1).findOne();

            ObjectNode result = Json.newObject();
            result.put(CODE,CODE200);
            result.put("status", tt != null);
            return ok(result);
        });
    }

    /**
     * @api {POST} /v1/front/tk/school/list/  02 学校列表
     * @apiName schoolList
     * @apiGroup New
     *
     * @apiDescription 学校信息列表
     *
     * @apiSuccess (Success 200){int} code 200
     * @apiSuccess (Success 200) {boolean} list 学校数据
     * @apiSuccessExample {json} 响应示例:
     * {
     *     "code":200
     *     "list":[
     *          {
     *             "id":0,
     *             "campusName":"",
     *             "address":"",
     *             "phone":"",
     *             "principal":"",
     *             "capacity":"",
     *             "establishDate":"",
     *             "status":"",
     *             "createTime":"",
     *             "updateTime":""
     *          }
     *     ]
     * }
     *
     */
    public CompletionStage<Result> schoolList(Http.Request request){
        return CompletableFuture.supplyAsync(()->{
            List<Campus> list = Campus.find.query().where().findList();

            ObjectNode result = Json.newObject();
            result.put(CODE,CODE200);
            result.set("list", Json.toJson(list));
            return ok(result);
        });
    }
}