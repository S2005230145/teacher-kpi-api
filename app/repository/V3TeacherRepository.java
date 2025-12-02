package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.v3.*;
import models.user.Role;
import models.user.User;
import utils.Pair;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Singleton
public class V3TeacherRepository {

    public Pair<Boolean, List<String>> addKpi(List<KPI> kpiList){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        try{
            DB.saveAll(kpiList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入KPI出错: "+e);
            transaction.rollback();
        }
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    public Pair<Boolean, List<String>> addKpiSingle(KPI kpi){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        try{
            kpi.save();
        }catch (Exception e){
            errorMsg.add("kpi添加出错: "+e);
            transaction.rollback();
        }

        List<Indicator> oldIndicatorList = kpi.getIndicatorList();
        oldIndicatorList.forEach(oldIndicator->{
            oldIndicator.setKpiId(kpi.getId());
        });

        errorMsg.addAll(this.addAll(oldIndicatorList).second());

        transaction.commit();

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    public Pair<Boolean, List<String>> addAll(List<Indicator> indicatorParamList){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();

        //添加队列
        List<Indicator> addIndicatorList=new ArrayList<>();
        List<Element> addElementList=new ArrayList<>();
        List<Content> addContentList=new ArrayList<>();

        indicatorParamList.forEach(indicator -> {
            Indicator newIndicator=new Indicator();
            newIndicator.setIndicatorName(indicator.getIndicatorName());
            newIndicator.setKpiId(indicator.getKpiId());
            newIndicator.setSubName(indicator.getSubName());
            addIndicatorList.add(newIndicator);
        });

        try{
            DB.saveAll(addIndicatorList);
        } catch (Exception e) {
            errorMsg.add("插入评价指标出错: "+e);
            transaction.rollback();
        }

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    public Pair<PagedList<KPI>,List<String>> getKpiList(ExpressionList<KPI> expressionList, int current,int size){
        PagedList<KPI> pagedList = expressionList.setFirstRow(current-1).setMaxRows(size).findPagedList();
        List<String> msg=new ArrayList<>();

        pagedList.getList().forEach(kpi -> {
            ExpressionList<Indicator> expressionList1 = Indicator.find.query().where().eq("kpi_id", kpi.getId());
            kpi.setIndicatorList(this.getList(expressionList1,1,10).first().getList());
        });

        return new Pair<>(pagedList,msg);
    }

    public Pair<PagedList<Indicator>,List<String>> getList(ExpressionList<Indicator> expressionList, int current,int size){
        PagedList<Indicator> pagedList = expressionList.setFirstRow(current-1).setMaxRows(size).findPagedList();
        List<String> msg=new ArrayList<>();

        //All_Element
        List<Element> elementList=Element.find.query().where().in("indicator_id", pagedList.getList().stream().map(Indicator::getId).toList()).findList();
        //All_Content
        List<Content> contentList=Content.find.query().where().in("element_id",elementList.stream().map(Element::getId).toList()).findList();
        pagedList.getList().forEach(indicator -> {
            List<Element> filterElementList = elementList.stream().filter(element -> Objects.equals(element.getIndicatorId(), indicator.getId())).toList();
            filterElementList.forEach(element -> {
                element.setContentList(contentList.stream().filter(content -> Objects.equals(content.getElementId(), element.getId())).toList());
                if(element.getContentList().isEmpty()) msg.add("要素--"+element.getElement()+" 的评价内容为空");
            });
            indicator.setElementList(filterElementList);
            if(indicator.getElementList().isEmpty()) msg.add("指标--"+indicator.getIndicatorName()+" 的评价要素为空");
        });

        return new Pair<>(pagedList,msg);
    }

    public Pair<PagedList<Element>,List<String>> getElementList(ExpressionList<Element> expressionList, int current,int size){
        PagedList<Element> pagedList = expressionList.setFirstRow(current-1).setMaxRows(size).findPagedList();
        List<String> msg=new ArrayList<>();


        return new Pair<>(pagedList,msg);
    }

    public Pair<PagedList<Content>,List<String>> getContentList(ExpressionList<Content> expressionList, int current,int size){
        PagedList<Content> pagedList = expressionList.setFirstRow(current-1).setMaxRows(size).findPagedList();
        List<String> msg=new ArrayList<>();

        return new Pair<>(pagedList,msg);
    }

    public Pair<Boolean, List<String>> dispatchAll(List<Long> ids,Long kpiId){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        //添加列表
        List<TeacherKPIScore> addTeacherKPIScoreList=new ArrayList<>();
        List<TeacherElementScore> addTeacherElementScoreList=new ArrayList<>();
        List<TeacherContentScore> addTeacherContentScoreList=new ArrayList<>();
        List<TeacherIndicatorScore> addTeacherIndicatorScoreList=new ArrayList<>();

        KPI kpi = KPI.find.query().where().eq("id",kpiId).setMaxRows(1).findOne();
        //=================
        List<Indicator> list = Indicator.find.query().where().eq("kpi_id", kpiId).findList();
        kpi.setIndicatorList(list);
        //=================
        List<Long> indicatorIds = Objects.requireNonNull(kpi).getIndicatorList().stream().map(Indicator::getId).toList();

        List<Element> elementList=Element.find.query().where().in("indicator_id",indicatorIds).findList();
        List<Long> elementIds = Objects.requireNonNull(elementList).stream().map(Element::getId).toList();

        List<Content> contentList=Content.find.query().where().in("element_id",elementIds).findList();

        //教师id下发
        ids.forEach(id->{
            //kpi下发
            TeacherKPIScore teacherKPIScore = new TeacherKPIScore();
            teacherKPIScore.setUserId(id);
            teacherKPIScore.setKpiId(kpiId);
            teacherKPIScore.setScore(100.0);
            addTeacherKPIScoreList.add(teacherKPIScore);
            //indicator下发
            list.forEach(indicator -> {
                TeacherIndicatorScore tis = new TeacherIndicatorScore();
                tis.setUserId(id);
                tis.setIndicatorId(indicator.getId());
                tis.setKpiId(kpiId);
                tis.setScore(indicator.getScore());
                addTeacherIndicatorScoreList.add(tis);
            });
            //element下发
            elementList.forEach(element->{
                TeacherElementScore teacherElementScore=new TeacherElementScore();
                teacherElementScore.setUserId(id);
                teacherElementScore.setKpiId(kpiId);
                teacherElementScore.setElementId(element.getId());
                teacherElementScore.setIndicatorId(element.getIndicatorId());
                teacherElementScore.setScore(element.getScore());
                if(element.getType()==1){//上级
                    teacherElementScore.setRobotScore(element.getScore());
                    teacherElementScore.setFinalScore(element.getScore());
                }
                addTeacherElementScoreList.add(teacherElementScore);
            });
            //content下发
            contentList.forEach(content->{
                TeacherContentScore teacherContentScore=new TeacherContentScore();
                teacherContentScore.setUserId(id);
                teacherContentScore.setElementId(content.getElementId());
                teacherContentScore.setContentId(content.getId());
                if(content.getType()==1){//需要材料
                    teacherContentScore.setTime(0);
                    teacherContentScore.setScore(null);
                }else{
                    teacherContentScore.setTime(1);
                    teacherContentScore.setScore(content.getScore());
                }
                addTeacherContentScoreList.add(teacherContentScore);
            });
        });
        try{
            DB.saveAll(addTeacherContentScoreList);
        }
        catch (Exception e){
            errorMsg.add("下发内容出错: "+e);
            transaction.rollback();
        }

        //去材料
        //将需要材料的content提出
        Set<Long> contentIds = contentList.stream()
                .filter(v1 -> v1.getType() == 1)
                .toList()
                .stream()
                .map(Content::getId)
                .collect(Collectors.toSet());
        List<TeacherContentScore> tcsList = addTeacherContentScoreList.stream()
                .filter(v1 -> contentIds.contains(v1.getContentId()))
                .toList();
        addTeacherElementScoreList.forEach(tes->{
            Element element = elementList.stream()
                    .filter(v1 -> Objects.equals(v1.getId(), tes.getElementId()))
                    .findFirst().orElse(null);
            if(element!=null){
                //先求相关元素对应的材料的分数总和
                double score = tcsList.stream()
                        .filter(v1 -> v1.getScore()!=null&&v1.getScore()>=0&&Objects.equals(v1.getElementId(), tes.getElementId()))
                        .map(TeacherContentScore::getScore)
                        .mapToDouble(Double::valueOf)
                        .sum();
                //然后减去
                tes.setRobotScore(element.getScore()-score);
                tes.setScore(element.getScore()-score);
                tes.setFinalScore(element.getScore()-score);
            }
        });
        try{
            DB.saveAll(addTeacherElementScoreList);
        }
        catch (Exception e){
            errorMsg.add("下发要素出错: "+e);
            transaction.rollback();
        }

        //过滤出需要上级评分的要素
        List<Element> elementScoreListByType1 = elementList.stream()
                .filter(v1 -> v1.getType() == 1)
                .toList();
        addTeacherIndicatorScoreList.forEach(tis->{
            Indicator indicator = list.stream()
                    .filter(v1 -> Objects.equals(v1.getId(), tis.getIndicatorId()))
                    .findFirst().orElse(null);
            if(indicator!=null){
                //先求相关元素上级评分分数总和
                double score = elementScoreListByType1.stream()
                        .filter(v1 -> v1.getScore()!=null&&v1.getScore()>=0&&Objects.equals(v1.getIndicatorId(), tis.getIndicatorId()))
                        .map(Element::getScore)
                        .mapToDouble(Double::valueOf)
                        .sum();
                //然后减去
                tis.setScore(indicator.getScore()-score);
                tis.setFinalScore(indicator.getScore());
            }
        });

        try{
            DB.saveAll(addTeacherIndicatorScoreList);
        }
        catch (Exception e){
            errorMsg.add("下发Indicator出错: "+e);
            transaction.rollback();
        }

        //求KPI分数
        double sum = addTeacherIndicatorScoreList.stream()
                .filter(v1->v1.getScore()!=null&&v1.getScore()<5000&&v1.getScore()>-5000)
                .map(TeacherIndicatorScore::getScore)
                .mapToDouble(Double::valueOf)
                .sum();
        addTeacherKPIScoreList.forEach(tks->{
            tks.setScore(sum);
            tks.setFinalScore(sum);
        });

        try{
            DB.saveAll(addTeacherKPIScoreList);
        }
        catch (Exception e){
            errorMsg.add("下发KPI出错: "+e);
            transaction.rollback();
        }

        List<Role> allRole = Role.find.all();
        List<User> allUser = User.find.all();
        allUser.forEach(user->{
            user.setRole(allRole.stream().filter(v1-> Objects.equals(v1.getId(), user.getRoleId())).findFirst().orElse(null));
        });
        String parentIds = allUser.stream()
                .filter(v1 -> v1.getRole().getNickName().contains("管理员") || v1.getRole().getNickName().contains("领导"))
                .map(v1 -> v1.getId().toString())
                .collect(Collectors.joining(","));
        List<TeacherTask> addTeacherTaskList=new ArrayList<>();
        //上级评分任务下发
        List<Element> elementListByType = elementList.stream()
                .filter(v1 -> v1.getType() == 1)
                .toList();
        Set<Long> indicatorIdsByType = elementListByType.stream()
                .map(Element::getIndicatorId)
                .collect(Collectors.toSet());
        ids.forEach(userId->{
            indicatorIdsByType.forEach(indicatorId->{
                TeacherTask teacherTask = new TeacherTask();
                teacherTask.setUserId(userId);
                teacherTask.setParentIds(parentIds);
                teacherTask.setStatus("待完成");
                addTeacherIndicatorScoreList.stream()
                        .filter(v1 -> Objects.equals(v1.getIndicatorId(), indicatorId))
                        .findFirst().ifPresent(teacherIndicatorScore -> teacherTask.setTisId(teacherIndicatorScore.getId()));
                addTeacherTaskList.add(teacherTask);
            });
        });

        try{
            DB.saveAll(addTeacherTaskList);
        }
        catch (Exception e) {
            transaction.rollback();
            errorMsg.add("下发上级任务出错:"+e);
        }
        transaction.commit();
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    //传过来无需elementId
    public Pair<Boolean, List<String>> grade(Long userId,Long kpiId,List<TeacherContentScore> teacherContentScores){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();

        List<TeacherContentScore> teacherContentScoreList = TeacherContentScore.find.query().where().eq("user_id", userId).in("content_id",teacherContentScores.stream().map(TeacherContentScore::getContentId).toList()).findList();
        List<Long> contentIds = teacherContentScores.stream().map(TeacherContentScore::getContentId).toList();
        List<Content> contentList = Content.find.query().where().in("id", contentIds).findList();
        List<Element> elementList=Element.find.query().where().in("id",contentList.stream().map(Content::getElementId).toList()).findList();
        if(teacherContentScoreList.isEmpty()){
            errorMsg.add("没有该教师的评测内容");
        }
        else{
            teacherContentScoreList.forEach(tcs->{
                //计算逻辑
                TeacherContentScore teacherContentScore = teacherContentScores.stream().filter(v1 -> Objects.equals(v1.getContentId(), tcs.getContentId())).findFirst().orElse(null);
                Content content = contentList.stream().filter(v1 -> Objects.equals(v1.getId(), tcs.getContentId())).findFirst().orElse(null);

                if(teacherContentScore!=null&&content!=null){
                    Element element = elementList.stream().filter(v1 -> Objects.equals(v1.getId(), content.getElementId())).findFirst().orElse(null);
                    double score = teacherContentScore.getTime()*content.getScore();
                    if(element!=null){
                        if(score>element.getScore()){
                            score=element.getScore();
                        }else if(score<=0.0){
                            score=0.0;
                        }
                    }
                    tcs.setScore(score);
                    tcs.setTime(teacherContentScore.getTime());
                }else{
                    errorMsg.add("warning---该内容分数不存在");
                }
                if(tcs.getScore()==null){
                    errorMsg.add("warning---该分数未评测，默认0分");
                    tcs.setScore(0.0);
                }
            });
            try{
                DB.updateAll(teacherContentScoreList);
            } catch (Exception e) {
                errorMsg.add("内容分数更新失败");
                transaction.rollback();
            }

            //更新element分数
            Set<Long> elementIds = contentList.stream().map(models.school.kpi.v3.Content::getElementId).collect(Collectors.toSet());
            List<TeacherElementScore> teacherElementScoreList = TeacherElementScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherElementScore> filterTeacherElementScoreList = teacherElementScoreList.stream().filter(v1 -> elementIds.contains(v1.getElementId())).toList();
            filterTeacherElementScoreList.forEach(tes->{
                tes.setScore(teacherContentScoreList.stream().filter(v1-> Objects.equals(v1.getElementId(), tes.getElementId())).map(TeacherContentScore::getScore).mapToDouble(Double::doubleValue).sum());
            });
            try{
                DB.updateAll(filterTeacherElementScoreList);
            } catch (Exception e) {
                errorMsg.add("要素分数更新失败");
                transaction.rollback();
            }
            //更新Indicator分数
            Set<Long> indicatorIds = elementList.stream().map(Element::getIndicatorId).collect(Collectors.toSet());
            List<TeacherIndicatorScore> teacherIndicatorScoreList=TeacherIndicatorScore.find.query().where().eq("user_id", userId).findList();
            List<TeacherIndicatorScore> filterTeacherIndicatorScoreList = teacherIndicatorScoreList.stream().filter(v1 -> indicatorIds.contains(v1.getIndicatorId())).toList();
            List<TeacherElementScore> list1 = teacherElementScoreList.stream().filter(v1 ->indicatorIds.contains(v1.getIndicatorId())).toList();
            filterTeacherIndicatorScoreList.forEach(tis->{
                tis.setScore(list1.stream().filter(v1-> v1.getScore()!=null&&Objects.equals(v1.getIndicatorId(), tis.getIndicatorId())).map(TeacherElementScore::getScore).mapToDouble(Double::doubleValue).sum());
            });
            try{
                DB.updateAll(filterTeacherIndicatorScoreList);
            } catch (Exception e) {
                errorMsg.add("指标更新失败");
                transaction.rollback();
            }

            //更新kpi分数(总分)
            TeacherKPIScore tks = TeacherKPIScore.find.query().where().eq("user_id", userId).eq("kpi_id", kpiId).setMaxRows(1).findOne();
            if(tks!=null){
                tks.setScore(teacherIndicatorScoreList.stream().map(TeacherIndicatorScore::getScore).filter(Objects::nonNull).mapToDouble(Double::doubleValue).sum());
                try{
                    tks.update();
                    transaction.commit();
                } catch (Exception e) {
                    errorMsg.add("kpi分数更新失败");
                    transaction.rollback();
                }
            }else{
                errorMsg.add("该教师无KPI");
            }
        }

        errorMsg.addAll(this.autoGrade());

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    //自动评分
    public List<String> autoGrade(){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        List<Element> elementList = Element.find.query().where().in("type",1).findList();
        Transaction transaction = DB.beginTransaction();

        if(elementList.isEmpty()){
            return List.of("warning---没有自动评分要素项");
        }
        Set<Long> ids = elementList.stream().map(Element::getId).collect(Collectors.toSet());

        List<TeacherElementScore> teacherElementScoreList = TeacherElementScore.find.query().where().in("element_id", ids).findList();
        List<TeacherContentScore> teacherContentScoresList = TeacherContentScore.find.query().where().in("element_id", ids).findList();

        teacherElementScoreList.forEach(tes->{
            List<TeacherContentScore> filterTCSList = teacherContentScoresList.stream().filter(v1 -> Objects.equals(tes.getId(), v1.getElementId())).toList();
            List<Double> contentScoreList =filterTCSList.stream().map(TeacherContentScore::getScore).filter(Objects::nonNull).toList();
            //TODO具体系统评测逻辑需确定
            if(contentScoreList.isEmpty()){
                tes.setScore(this.randomDoubleInRange(0,10));
            }else{
                //当出现用户评分时，累加用户评分以及随机数
                double tmpScore=contentScoreList.stream().mapToDouble(Double::doubleValue).sum();
                tes.setScore(tmpScore+this.randomDoubleInRange(0,10-tmpScore));
            }
        });
        try{
            DB.updateAll(teacherElementScoreList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("自动评分出错");
            transaction.rollback();
        }
        return errorMsg;
    }

    public Pair<Boolean, List<String>> withDraw(List<Long> teacherIds){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();

        List<TeacherKPIScore> deleteTeacherKPIScoreList=TeacherKPIScore.find.query().where().in("user_id",teacherIds).findList();
        List<TeacherIndicatorScore> deleteTeacherIndicatorScoreList=TeacherIndicatorScore.find.query().where().in("user_id",teacherIds).findList();
        List<TeacherElementScore> deleteTeacherElementScoreList=TeacherElementScore.find.query().where().in("user_id",teacherIds).findList();
        List<TeacherContentScore> deleteTeacherContentScoreList=TeacherContentScore.find.query().where().in("user_id",teacherIds).findList();
        List<TeacherTask> deleteTeacherTaskList=TeacherTask.find.query().where().in("user_id",teacherIds).findList();

        try {
            DB.deleteAll(deleteTeacherKPIScoreList);
        } catch (Exception e) {
            errorMsg.add("撤销KPI出错:"+e);
            transaction.rollback();
        }
        try {
            DB.deleteAll(deleteTeacherIndicatorScoreList);
        } catch (Exception e) {
            errorMsg.add("撤销Indicator出错:"+e);
            transaction.rollback();
        }
        try {
            DB.deleteAll(deleteTeacherElementScoreList);
        } catch (Exception e) {
            errorMsg.add("撤销element出错:"+e);
            transaction.rollback();
        }
        try {
            DB.deleteAll(deleteTeacherContentScoreList);
        } catch (Exception e) {
            errorMsg.add("撤销content出错:"+e);
            transaction.rollback();
        }
        try {
            DB.deleteAll(deleteTeacherTaskList);
        } catch (Exception e) {
            errorMsg.add("撤销task出错:"+e);
            transaction.rollback();
        }

        transaction.commit();
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    //==========================工具==========================
    //获取所有要素
    private List<Pair<String,List<Element>>> getElementList(List<Indicator> indicatorParamList){
        List<Pair<String,List<Element>>> pairList=new ArrayList<>();
        indicatorParamList.forEach(indicator -> {
            pairList.add(new Pair<>(indicator.getIndicatorName(),indicator.getElementList()));
        });
        return pairList;
    }

    //获取所有要素里层的Content
    private List<Pair<String,List<Content>>> getContentList(List<Indicator> indicatorParamList){
        List<Pair<String,List<Content>>> pairList=new ArrayList<>();
        //一层
        indicatorParamList.forEach(indicator -> {
            indicator.getElementList().forEach(element -> {
                List<Content> contentList=this.getContentItem(element.getContentList());
                pairList.add(new Pair<>(element.getElement(),contentList));
            });
        });
        return pairList;
    }

    //评价内容分割
    private List<Content> getContentItem(List<Content> contents){
        List<Content> contentList=new ArrayList<>();
        contents.forEach(content -> {
            if(content.getContent().contains("@#$")){
                List.of(content.getContent().split("@#\\$")).forEach(name->{
                    Content newcontent = new Content();
                    newcontent.setElementId(content.getElementId());
                    newcontent.setContent(name);
                    contentList.add(newcontent);
                });
            }else if(content.getContent().contains("、")){
                List.of(content.getContent().split("、")).forEach(name->{
                    Content newcontent = new Content();
                    newcontent.setElementId(content.getElementId());
                    newcontent.setContent(name);
                    contentList.add(newcontent);
                });
            }else{
                contentList.add(content);
            }
        });
        return contentList;
    }

    private Double randomDoubleInRange(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("最小值必须小于最大值");
        }

        // 检测区间是否非常小（如 10-10.0000）
        if (Math.abs(max - min) < 0.0001) {
            return 0.0; // 返回固定值 0
        }

        Random random = new Random();
        double value = min + (max - min) * random.nextDouble();
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }

    private double generateRandomDouble(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("最小值必须小于最大值");
        }

        double randomValue = ThreadLocalRandom.current().nextDouble(min, max);
        // 保留两位小数
        return Math.round(randomValue * 100.0) / 100.0;
    }
}
