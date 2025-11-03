package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.v3.*;
import utils.Pair;

import java.text.DecimalFormat;
import java.util.*;
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

        List<Pair<String, List<Element>>> elementListParam=this.getElementList(indicatorParamList);
        addIndicatorList.forEach(indicatorParam-> {
            List<Element> list = Objects.requireNonNull(elementListParam.stream().filter(element -> Objects.equals(element.first(), indicatorParam.getIndicatorName())).findFirst().orElse(null)).second();
            list.forEach(elementParam -> {
                Element element=new Element();
                element.setIndicatorId(indicatorParam.getId());
                element.setElement(elementParam.getElement());
                element.setCriteria(elementParam.getCriteria());
                element.setType(0);
                addElementList.add(element);
            });
        });
        try{
            DB.saveAll(addElementList);
        } catch (Exception e) {
            errorMsg.add("插入评价要素出错: "+e);
            transaction.rollback();
        }

        List<Pair<String,List<Content>>> contentList=this.getContentList(indicatorParamList);
        addElementList.forEach(elementParam->{
            Pair<String, List<Content>> stringListPair = Objects.requireNonNull(contentList.stream().filter(v1 -> Objects.equals(v1.first(), elementParam.getElement())).findFirst().orElse(null));
            stringListPair.second().forEach(contentParam -> {
                Content content=new Content();
                content.setElementId(elementParam.getId());
                content.setContent(contentParam.getContent());
                addContentList.add(content);
            });
        });
        try{
            DB.saveAll(addContentList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入评价内容出错: "+e);
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

        List<Long> list = pagedList.getList().stream().map(Element::getId).toList();
        List<Content> contentList = Content.find.query().where().in("element_id",list).findList();
        pagedList.getList().forEach(ele -> {
            ele.setContentList(contentList.stream().filter(v1-> Objects.equals(v1.getElementId(), ele.getId())).toList());
        });

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

        KPI kpi = KPI.find.query().where().eq("id",kpiId).setMaxRows(1).findOne();
        System.out.println(kpi);
        //=================
        List<Indicator> list = this.getList(Indicator.find.query().where().eq("kpiId", kpiId), 1, 1000).first().getList();
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
            addTeacherKPIScoreList.add(teacherKPIScore);
            //element下发
            elementIds.forEach(elementId->{
                TeacherElementScore teacherElementScore=new TeacherElementScore();
                teacherElementScore.setUserId(id);
                teacherElementScore.setKpiId(kpiId);
                teacherElementScore.setElementId(elementId);
                addTeacherElementScoreList.add(teacherElementScore);
            });
            //content下发
            contentList.forEach(content->{
                TeacherContentScore teacherContentScore=new TeacherContentScore();
                teacherContentScore.setUserId(id);
                teacherContentScore.setElementId(content.getElementId());
                teacherContentScore.setContentId(content.getId());
                addTeacherContentScoreList.add(teacherContentScore);
            });
        });
        try{
            DB.saveAll(addTeacherKPIScoreList);
        }catch (Exception e){
            errorMsg.add("下发KPI出错: "+e);
            transaction.rollback();
        }
        try{
            DB.saveAll(addTeacherElementScoreList);
        }catch (Exception e){
            errorMsg.add("下发要素出错: "+e);
            transaction.rollback();
        }
        try{
            DB.saveAll(addTeacherContentScoreList);
            transaction.commit();
        }catch (Exception e){
            errorMsg.add("下发内容出错: "+e);
            transaction.rollback();
        }

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    //传过来无需elementId
    public Pair<Boolean, List<String>> grade(Long userId,List<TeacherContentScore> teacherContentScores){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();

        List<TeacherContentScore> teacherContentScoreList = TeacherContentScore.find.query().where().eq("user_id", userId).in("content_id",teacherContentScores.stream().map(TeacherContentScore::getContentId).toList()).findList();
        if(teacherContentScoreList.isEmpty()){
            errorMsg.add("没有该教师的评测内容");
        }
        else{
            teacherContentScoreList.forEach(tcs->{
                tcs.setScore(Objects.requireNonNull(teacherContentScores.stream().filter(v1 -> Objects.equals(v1.getContentId(), tcs.getContentId())).findFirst().orElse(null)).getScore());
                if(tcs.getScore()==null){
                    errorMsg.add("warning---该分数未评测，默认0分");
                    tcs.setScore(0.0);
                }
            });
            try{
                DB.updateAll(teacherContentScoreList);
            } catch (Exception e) {
                errorMsg.add("内容分数更新失败");
            }

            //更新element分数
            List<Long> contentIds = teacherContentScores.stream().map(TeacherContentScore::getContentId).toList();
            Set<Long> elementIds = Content.find.query().where().in("id", contentIds).findList().stream().map(Content::getElementId).collect(Collectors.toSet());
            List<TeacherElementScore> teacherElementScoreList = TeacherElementScore.find.query().where().eq("user_id", userId).in("element_id",elementIds).findList();

            teacherElementScoreList.forEach(tes->{
                tes.setScore(teacherContentScoreList.stream().filter(v1-> Objects.equals(v1.getElementId(), tes.getElementId())).map(TeacherContentScore::getScore).mapToDouble(Double::doubleValue).sum());
            });
            try{
                DB.updateAll(teacherElementScoreList);
            } catch (Exception e) {
                errorMsg.add("要素分数更新失败");
            }

            //更新kpi分数(总分)
            List<Long> list = teacherElementScoreList.stream().map(TeacherElementScore::getKpiId).toList();
            TeacherKPIScore tks = TeacherKPIScore.find.query().where().eq("user_id", userId).in("kpiId", list).setMaxRows(1).findOne();
            if(tks!=null){
                tks.setScore(teacherElementScoreList.stream().map(TeacherElementScore::getScore).mapToDouble(Double::doubleValue).sum());
                try{
                    tks.update();
                    transaction.commit();
                } catch (Exception e) {
                    errorMsg.add("kpi分数更新失败");
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
        List<TeacherElementScore> deleteTeacherElementScoreList=TeacherElementScore.find.query().where().in("user_id",teacherIds).findList();
        List<TeacherContentScore> deleteTeacherContentScoreList=TeacherContentScore.find.query().where().in("user_id",teacherIds).findList();
        try {
            DB.deleteAll(deleteTeacherKPIScoreList);
        } catch (Exception e) {
            errorMsg.add("撤销KPI出错:"+e);
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
        transaction.commit();
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }
    //工具
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
}
