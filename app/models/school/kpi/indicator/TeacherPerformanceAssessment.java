package models.school.kpi.indicator;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import utils.Pair;

import java.util.List;
import java.util.stream.Collectors;

//评价要素及其余内容
@Entity
@Table(name = "teacher_performance_assessment")
public class TeacherPerformanceAssessment extends Model {

    // 唯一标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    // 评价要素id集合(存储不同评价要素共享同一评价标准和得分时的id集合,以逗号分割)
    @Column(name = "element_ids")
    public String elementIds;
    @Transient
    public EvaluationElement evaluationElement;

    // 评价指标ID(对应的)
    @Column(name="evaluation_id")
    public Long evaluationId;

    //评价内容
    @Column(name = "content_ids")
    public String contentIds;
    @Transient
    public List<EvaluationContent> evaluationContentList;

    // 评价标准
    @Column(name = "evaluation_standard")
    public String evaluationStandard;

    // 得分
    @Column(name = "score")
    public Double score;

    public List<EvaluationElement> getEvaluationElementBlock(){
        return this.elementIds.split(",").length >= 1?EvaluationElement.find.query().where().in("id",elementIds).findList():null;
    }

    public String getDetailContent(){
        return evaluationContentList.stream().map(EvaluationContent::getEvaluationContent).collect(Collectors.joining("、"));
    }

    public static Finder<Long, TeacherPerformanceAssessment> find =
            new Finder<>(TeacherPerformanceAssessment.class);

}
