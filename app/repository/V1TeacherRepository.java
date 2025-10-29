package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.param.AddEvaluationParam;
import models.school.kpi.param.TPAParam;
import models.school.kpi.v1.KPI;
import models.school.kpi.v1.indicator.EvaluationContent;
import models.school.kpi.v1.indicator.EvaluationElement;
import models.school.kpi.v1.indicator.EvaluationIndicator;
import models.school.kpi.v1.indicator.TeacherPerformanceAssessment;
import utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Singleton
public class V1TeacherRepository {

    /**
     * 获取List和pageList
     * @param expressionList KPI条件表达式
     * @return 普通KPI集合,带分页的KPI集合
     */
    public Pair<List<KPI>, PagedList<KPI>> getKPIList(ExpressionList<KPI> expressionList,int Limit){
        //查询KPI队列
        PagedList<KPI> kpiListPaged=expressionList.setMaxRows(Limit).findPagedList();
        List<KPI> kpiList = kpiListPaged.getList();
        //查询EvaluationIndicator队列
        List<EvaluationIndicator> evaluationIndicatorList=EvaluationIndicator.find.query().where().in("kpi_id",kpiList.stream().map(KPI::getId).toList()).findList();
        //查询TeacherPerformanceAssessment的全部队列
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentList=TeacherPerformanceAssessment.find.query().where().in("evaluation_id",evaluationIndicatorList.stream().map(EvaluationIndicator::getId).toList()).findList();
        //过滤TeacherPerformanceAssessment的共享队列teacherPerformanceAssessmentBlock
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentBlockList=teacherPerformanceAssessmentList.stream().filter(TeacherPerformanceAssessment::isBlock).toList();
        //过滤全部队列TeacherPerformanceAssessment的单一队列teacherPerformanceAssessmentSingle
        HashSet<TeacherPerformanceAssessment> tmpSet = new HashSet<>(teacherPerformanceAssessmentBlockList);
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentSingleList = teacherPerformanceAssessmentList.stream().filter(e -> !tmpSet.contains(e)).toList();

        //注入EvaluationElement
        List<EvaluationElement> evaluationElements=EvaluationElement.find.query().where().in("id",teacherPerformanceAssessmentBlockList.stream().map(TeacherPerformanceAssessment::getElementIds).toList()).findList();
        //注入EvaluationContent
        List<EvaluationContent> evaluationContentList=EvaluationContent.find.query().where().in("id",teacherPerformanceAssessmentList.stream().map(TeacherPerformanceAssessment::getContentIds).toList()).findList();
        if(!teacherPerformanceAssessmentBlockList.isEmpty()){
            teacherPerformanceAssessmentBlockList.forEach(var1-> {
                var1.setEvaluationElementList(evaluationElements.stream().filter(var2->var1.getElementIds().contains(String.valueOf(var2.getId()))).toList());
                var1.setEvaluationContentList(evaluationContentList.stream().filter(var2->var1.getContentIds().contains(String.valueOf(var2.getId()))).toList());
            });
        }
        if(!teacherPerformanceAssessmentSingleList.isEmpty()){
            teacherPerformanceAssessmentSingleList.forEach(var-> {
                var.setEvaluationElement(evaluationElements.stream().filter(var1-> String.valueOf(var1.getId()).equals(var.getElementIds())).findFirst().orElse(null));
                var.setEvaluationContentList(evaluationContentList.stream().filter(var2->var.getContentIds().contains(String.valueOf(var2.getId()))).toList());
            });
        }

        //融合
        List<TeacherPerformanceAssessment> tpaList=new ArrayList<>();
        tpaList.addAll(teacherPerformanceAssessmentSingleList);
        tpaList.addAll(teacherPerformanceAssessmentBlockList);

        //注入EvaluationIndicatorList
        evaluationIndicatorList.forEach(evaluationIndicator ->evaluationIndicator.setTeacherPerformanceAssessmentList(tpaList.stream().filter(var->var.getEvaluationId()==evaluationIndicator.getId()).toList()));

        //注入kpiList
        Consumer<KPI> kpiFunction=kpi-> kpi.setEvaluationIndicatorList(evaluationIndicatorList.stream().filter(evaluationIndicator -> Objects.equals(evaluationIndicator.getKpiId(), kpi.getId())).toList());
        kpiList.forEach(kpiFunction);
        kpiListPaged.getList().forEach(kpiFunction);
        return new Pair<>(kpiList,kpiListPaged);
    }

    public KPI getKPI(ExpressionList<KPI> expressionList){
        KPI kpi = expressionList.setMaxRows(1).findOne();

        //查询EvaluationIndicator队列
        List<EvaluationIndicator> evaluationIndicatorList=EvaluationIndicator.find.query().where().eq("kpi_id",kpi.getId()).findList();
        //查询TeacherPerformanceAssessment的全部队列
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentList=TeacherPerformanceAssessment.find.query().where().in("evaluation_id",evaluationIndicatorList.stream().map(EvaluationIndicator::getId).toList()).findList();
        //过滤TeacherPerformanceAssessment的共享队列teacherPerformanceAssessmentBlock
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentBlockList=teacherPerformanceAssessmentList.stream().filter(TeacherPerformanceAssessment::isBlock).toList();
        //过滤全部队列TeacherPerformanceAssessment的单一队列teacherPerformanceAssessmentSingle
        HashSet<TeacherPerformanceAssessment> tmpSet = new HashSet<>(teacherPerformanceAssessmentBlockList);
        List<TeacherPerformanceAssessment> teacherPerformanceAssessmentSingleList = teacherPerformanceAssessmentList.stream().filter(e -> !tmpSet.contains(e)).toList();

        //注入EvaluationElement
        List<EvaluationElement> evaluationElements=EvaluationElement.find.query().where().in("id",teacherPerformanceAssessmentBlockList.stream().map(TeacherPerformanceAssessment::getElementIds).toList()).findList();
        //注入EvaluationContent
        List<EvaluationContent> evaluationContentList=EvaluationContent.find.query().where().in("id",teacherPerformanceAssessmentList.stream().map(TeacherPerformanceAssessment::getContentIds).toList()).findList();
        if(!teacherPerformanceAssessmentBlockList.isEmpty()){
            teacherPerformanceAssessmentBlockList.forEach(var1-> {
                var1.setEvaluationElementList(evaluationElements.stream().filter(var2->var1.getElementIds().contains(String.valueOf(var2.getId()))).toList());
                var1.setEvaluationContentList(evaluationContentList.stream().filter(var2->var1.getContentIds().contains(String.valueOf(var2.getId()))).toList());
            });
        }
        if(!teacherPerformanceAssessmentSingleList.isEmpty()){
            teacherPerformanceAssessmentSingleList.forEach(var-> {
                var.setEvaluationElement(evaluationElements.stream().filter(var1-> String.valueOf(var1.getId()).equals(var.getElementIds())).findFirst().orElse(null));
                var.setEvaluationContentList(evaluationContentList.stream().filter(var2->var.getContentIds().contains(String.valueOf(var2.getId()))).toList());
            });
        }

        //融合
        List<TeacherPerformanceAssessment> tpaList=new ArrayList<>();
        tpaList.addAll(teacherPerformanceAssessmentSingleList);
        tpaList.addAll(teacherPerformanceAssessmentBlockList);

        //注入EvaluationIndicatorList
        evaluationIndicatorList.forEach(evaluationIndicator ->evaluationIndicator.setTeacherPerformanceAssessmentList(tpaList.stream().filter(var->var.getEvaluationId()==evaluationIndicator.getId()).toList()));

        kpi.setEvaluationIndicatorList(evaluationIndicatorList);
        return kpi;
    }

    /**
     * 添加一种评价指标以及规则
     * @param addEvaluationParam 前端参数集
     * @return 添加状态，报错信息集合
     */
    public Pair<Boolean,List<String>> addAll(AddEvaluationParam addEvaluationParam){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        //添加队列
        List<EvaluationElement> addEvaluationElements=new ArrayList<>();
        List<EvaluationContent> addEvaluationContents=new ArrayList<>();
        List<TeacherPerformanceAssessment> addTeacherPerformanceAssessmentList=new ArrayList<>();

        List<TPAParam> tpaList = addEvaluationParam.getTpalist();
        tpaList.forEach(tpa->{
            addEvaluationElements.addAll(tpa.getEvaluationElementList());
            addEvaluationContents.addAll(tpa.getEvaluationContentList());
        });
        //插入评价指标
        EvaluationIndicator evaluationIndicator=new EvaluationIndicator();
        evaluationIndicator.setName(addEvaluationParam.getIndicatorName());
        evaluationIndicator.setSubName(addEvaluationParam.getIndicatorSubName());
        evaluationIndicator.setKpiId(addEvaluationParam.getKpiId());

        Transaction transaction = DB.beginTransaction();
        try{
            evaluationIndicator.save();
        } catch (Exception e) {
            errorMsg.add("插入评价指标出错: "+e);
        }
        //插入评价要素
        try{
            DB.saveAll(addEvaluationElements);
        } catch (Exception e) {
            errorMsg.add("插入评价要素出错: "+e);
        }
        //插入评价内容
        try{
            DB.saveAll(addEvaluationContents);
        } catch (Exception e) {
            errorMsg.add("插入评价内容出错: "+e);
        }

        //信息补充
        tpaList.forEach(tpaParam->{
            TeacherPerformanceAssessment tpa=new TeacherPerformanceAssessment();
            //添加评价要素id集合和相关信息
            String elementIds= addEvaluationElements.stream().filter(value->value.getId()!=0).map(value -> String.valueOf(value.getId())).collect(Collectors.joining(","));
            if(!elementIds.isEmpty()){
                tpa.setElementIds(elementIds);
            }else{
                errorMsg.add("warning--遍历TPA出错: 没有评价要素id集合，设置为单独模式");
                tpa.setElementIds(String.valueOf(tpa.getEvaluationElementList().stream().map(EvaluationElement::getId).findFirst().orElse(0L)));
            }

            //添加评价指标ID(父级ID)
            tpa.setEvaluationId(evaluationIndicator.getId());

            //添加评价内容id集合和相关信息
            String contentIds= addEvaluationContents.stream().filter(value->value.getId()!=0).map(value -> String.valueOf(value.getId())).collect(Collectors.joining(","));
            if(!contentIds.isEmpty()){
                tpa.setContentIds(contentIds);
            }else{
                errorMsg.add("遍历TPA出错: 没有评价内容id集合");
            }

            //添加评价标准
            tpa.setEvaluationStandard(tpaParam.getEvaluationStandard());

            //添加得分
            tpa.setScore(tpaParam.getScore());

            addTeacherPerformanceAssessmentList.add(tpa);
        });
        System.out.println(addTeacherPerformanceAssessmentList);
        //插入评价集合
        try{
            DB.saveAll(addTeacherPerformanceAssessmentList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入评价集合出错: "+e);
        }

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    /**
     * 绩效考核下发
     * @param userId 下发人
     * @param ids 接收者的id集合
     * @return 状态,报错信息与下发信息集合
     */
    public Pair<Boolean,List<String>> addTask(Long userId,List<Long> ids,Long kpiId){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        //添加队列
        List<KPI> addKPIList=new ArrayList<>();

        KPI orignalKpi = getKPI(KPI.find.query().where().eq("id", kpiId));
        ids.forEach(id->{
            KPI kpi = new KPI();
            kpi.setStartTime(orignalKpi.getStartTime());
            kpi.setEndTime(orignalKpi.getEndTime());
            kpi.setUserId(id);
            kpi.setParentId(userId);
            addKPIList.add(kpi);
        });
        //transaction.commit();
        //保存所有接受者的KPI
        try{
            DB.saveAll(addKPIList);
        } catch (Exception e) {
            errorMsg.add("插入KPI出错: "+e);
        }

        //此处使用并行流，可使得项目的时间复杂度变为O(n*m*k)/p
        //所有接受者
        List<String> booleanPairList=new ArrayList<>();
        addKPIList.parallelStream().forEach(kpi -> {
            //克隆数据给相关表
            kpi.getEvaluationIndicatorList().parallelStream().forEach(evaluationIndicator -> {
                AddEvaluationParam param = createAddEvaluationParam(kpi, evaluationIndicator);
                booleanPairList.addAll(this.addAll(param).second());
            });
        });

        //TODO通知接收者
        errorMsg.addAll(booleanPairList);
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    /**
     * 删除多个Kpi
     * @param KpiIds kpi的id集合
     * @return 删除状态，报错信息集合
     */
    public Pair<Boolean,List<String>> deleteKPI(List<Long> KpiIds){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        //删除队列
        List<KPI> deleteKpiList=KPI.find.query().where().in("id",KpiIds).findList();
        List<EvaluationIndicator> deleteEvaluationIndicatorList=EvaluationIndicator.find.query().where().in("kpi_id",KpiIds).findList();
        List<TeacherPerformanceAssessment> deletetpaList=TeacherPerformanceAssessment.find.query().where().in("evaluation_id",deleteEvaluationIndicatorList.stream().map(EvaluationIndicator::getId).toList()).findList();
        List<EvaluationElement> deleteEvaluationElementList=EvaluationElement.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getElementIds).toList()).findList();
        List<EvaluationContent> deleteEvaluationContentList=EvaluationContent.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getContentIds).toList()).findList();

        try{
            DB.deleteAll(deleteEvaluationElementList);
            DB.deleteAll(deleteEvaluationContentList);
        } catch (Exception e) {
            errorMsg.add("删除评价要素和评价内容出错: "+e);
        }
        try{
            DB.deleteAll(deletetpaList);
        } catch (Exception e) {
            errorMsg.add("删除tpa出错: "+e);
        }
        try{
            DB.deleteAll(deleteEvaluationIndicatorList);
        } catch (Exception e) {
            errorMsg.add("删除评价指标出错: "+e);
        }
        try{
            DB.deleteAll(deleteKpiList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("删除kpi出错: "+e);
        }

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }


    /**
     * 删除多个评价指标
     * @param indicatorIds indicator的id集合
     * @return 删除状态，报错信息集合
     */
    public Pair<Boolean,List<String>> deleteIndicator(List<Long> indicatorIds){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        //删除队列
        List<EvaluationIndicator> deleteEvaluationIndicatorList=EvaluationIndicator.find.query().where().in("id",indicatorIds).findList();
        List<TeacherPerformanceAssessment> deletetpaList=TeacherPerformanceAssessment.find.query().where().in("evaluation_id",deleteEvaluationIndicatorList.stream().map(EvaluationIndicator::getId).toList()).findList();
        List<EvaluationElement> deleteEvaluationElementList=EvaluationElement.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getElementIds).toList()).findList();
        List<EvaluationContent> deleteEvaluationContentList=EvaluationContent.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getContentIds).toList()).findList();
        try{
            DB.deleteAll(deleteEvaluationElementList);
            DB.deleteAll(deleteEvaluationContentList);
        } catch (Exception e) {
            errorMsg.add("删除评价要素和评价内容出错: "+e);
        }
        try{
            DB.deleteAll(deletetpaList);
        } catch (Exception e) {
            errorMsg.add("删除tpa出错: "+e);
        }
        try{
            DB.deleteAll(deleteEvaluationIndicatorList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("删除评价指标出错: "+e);
        }

        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }


    /**
     * 删除多个tpa
     * @param tpaIds tpa的id集合
     * @return 删除状态，报错信息集合
     */
    public Pair<Boolean,List<String>> deleteTPA(List<Long> tpaIds){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        List<TeacherPerformanceAssessment> deletetpaList=TeacherPerformanceAssessment.find.query().where().in("id",tpaIds).findList();
        List<EvaluationElement> deleteEvaluationElementList=EvaluationElement.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getElementIds).toList()).findList();
        List<EvaluationContent> deleteEvaluationContentList=EvaluationContent.find.query().where().in("id",deletetpaList.stream().map(TeacherPerformanceAssessment::getContentIds).toList()).findList();
        try{
            DB.deleteAll(deleteEvaluationElementList);
            DB.deleteAll(deleteEvaluationContentList);
        } catch (Exception e) {
            errorMsg.add("删除评价要素和评价内容出错: "+e);
        }
        try{
            DB.deleteAll(deletetpaList);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("删除tpa出错: "+e);
        }
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    /**
     * 评分
     * @param userId 用户ID
     * @param kpiId 用户对应的KPI的ID
     * @param evaluationContentList 对应评价内容的得分
     * @return 评分状态，报错信息集合
     */
    public Pair<Boolean,List<String>> audit(Long userId,Long kpiId,List<EvaluationContent> evaluationContentList){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        Transaction transaction = DB.beginTransaction();
        KPI kpi = KPI.find.query().where().eq("user_id", userId).eq("id",kpiId).setMaxRows(1).findOne();
        if(kpi!=null){
            List<Long> evaluationIndicatorIds = kpi.getEvaluationIndicatorList().stream().map(EvaluationIndicator::getId).toList();

            List<TeacherPerformanceAssessment> tpaList = TeacherPerformanceAssessment.find.query().where().in("evaluation_id", evaluationIndicatorIds).findList();
            String contentIds = tpaList.stream().map(TeacherPerformanceAssessment::getContentIds).collect(Collectors.joining(","));
            List<EvaluationContent> evaluationContents=EvaluationContent.find.query().where().in("id",contentIds).findList();
            evaluationContentList.forEach(evaluationContent -> this.updateValuesEvaluationContent(evaluationContents,evaluationContent.getEvaluationContent(),evaluationContent.getScore()));

            tpaList.forEach(tpa->{
                double totalScore = evaluationContents.stream().filter(evaluationContent -> tpa.getContentIds().contains(String.valueOf(evaluationContent.getId()))).map(EvaluationContent::getScore).mapToDouble(Double::doubleValue).sum();
                tpa.setScore(totalScore);
            });

            kpi.setTotalScore(tpaList.stream().map(TeacherPerformanceAssessment::getScore).mapToDouble(Double::doubleValue).sum());
            try{
                DB.updateAll(evaluationContents);
                DB.updateAll(tpaList);
                kpi.update();
                transaction.commit();
            } catch (Exception e) {
                errorMsg.add("评分出错: "+e);
            }

        }else{
            errorMsg.add("该用户没有KPI");
        }
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }

    // 工具
    private void updateValuesEvaluationContent(List<EvaluationContent> objectList, String targetName, Double score) {
        for (EvaluationContent obj : objectList) {
            if (obj.getEvaluationContent().equals(targetName)) {
                obj.setScore(score);
                break; // 如果name唯一，找到后可以跳出循环
            }
        }
    }

    private AddEvaluationParam createAddEvaluationParam(KPI kpi, EvaluationIndicator indicator) {
        AddEvaluationParam param = new AddEvaluationParam();
        param.setIndicatorName(indicator.getName());
        param.setIndicatorSubName(indicator.getSubName());
        param.setKpiId(kpi.getId());

        List<TPAParam> tpaParams = indicator.getTeacherPerformanceAssessmentList()
                .parallelStream()
                .map(this::convertToTPAParam)
                .collect(Collectors.toList());

        param.setTpalist(tpaParams);
        return param;
    }

    private TPAParam convertToTPAParam(TeacherPerformanceAssessment tpa) {
        TPAParam param = new TPAParam();
        if (tpa.getEvaluationElement() != null) {
            tpa.getEvaluationElement().setId(null);
            param.setEvaluationElementList(List.of(tpa.getEvaluationElement()));
        } else {
            tpa.getEvaluationElementList().forEach(ele -> ele.setId(null));
            param.setEvaluationElementList(tpa.getEvaluationElementList());
        }
        param.setEvaluationStandard(tpa.getEvaluationStandard());
        param.setScore(tpa.getScore());
        return param;
    }
}
