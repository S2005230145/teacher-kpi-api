package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import jakarta.inject.Singleton;
import models.school.kpi.param.AddEvaluationParam;
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
     * @param expressionList 条件表达式
     * @return List,pageList
     */
    public Pair<List<KPI>, PagedList<KPI>> getKPIList(ExpressionList<KPI> expressionList){
        //查询KPI队列
        PagedList<KPI> kpiListPaged=expressionList.findPagedList();
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

    public Pair<Boolean,List<String>> addAll(AddEvaluationParam addEvaluationParam){
        //错误列表
        List<String> errorMsg=new ArrayList<>();
        //添加队列
        List<EvaluationElement> addEvaluationElements=new ArrayList<>();
        List<EvaluationContent> addEvaluationContents=new ArrayList<>();

        List<TeacherPerformanceAssessment> tpaList = addEvaluationParam.getTPAList();
        tpaList.forEach(tpa->{
            addEvaluationElements.addAll(tpa.getEvaluationElementList());
            addEvaluationContents.addAll(tpa.getEvaluationContentList());
        });
        //插入评价指标
        try (Transaction transaction = EvaluationElement.find.db().beginTransaction()){
            DB.saveAll(addEvaluationElements);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入评价要素出错: "+e);
            return new Pair<>(false,errorMsg);
        }
        //插入评价内容
        try (Transaction transaction = EvaluationContent.find.db().beginTransaction()){
            DB.saveAll(addEvaluationContents);
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入评价内容出错: "+e);
            return new Pair<>(false,errorMsg);
        }
        //插入评价指标(父级)
        EvaluationIndicator evaluationIndicator=new EvaluationIndicator();
        evaluationIndicator.setName(addEvaluationParam.getIndicatorName());
        evaluationIndicator.setSubName(addEvaluationParam.getIndicatorSubName());
        evaluationIndicator.setKpiId(addEvaluationParam.getKpiId());
        try (Transaction transaction = EvaluationIndicator.find.db().beginTransaction()){
            evaluationIndicator.save();
            transaction.commit();
        } catch (Exception e) {
            errorMsg.add("插入评价内容出错: "+e);
            return new Pair<>(false,errorMsg);
        }

        tpaList.forEach(tpa->{
            String elementIds= tpa.getEvaluationElementList().stream().filter(value->value.getId()!=0).map(value -> String.valueOf(value.getId())).collect(Collectors.joining(","));
            if(!elementIds.isEmpty()){
                tpa.setElementIds(elementIds);
            }else{
                errorMsg.add("warning--遍历TPA出错: 没有评价要素id集合，设置为单独模式");
                tpa.setElementIds(String.valueOf(tpa.getEvaluationElementList().stream().map(EvaluationElement::getId).findFirst().orElse(0L)));
            }

            String contentIds= tpa.getEvaluationContentList().stream().filter(value->value.getId()!=0).map(value -> String.valueOf(value.getId())).collect(Collectors.joining(","));
            if(!contentIds.isEmpty()){
                tpa.setContentIds(contentIds);
            }else{
                errorMsg.add("遍历TPA出错: 没有评价内容id集合，设置为单独模式");
            }
            //父级ID
            tpa.setEvaluationId(evaluationIndicator.getId());
        });
        return new Pair<>(errorMsg.stream().filter(value -> !value.contains("warning")).toList().isEmpty(),errorMsg);
    }
}
