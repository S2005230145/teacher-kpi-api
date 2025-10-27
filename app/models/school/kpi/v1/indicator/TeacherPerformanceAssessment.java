package models.school.kpi.v1.indicator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价要素
 */
//评价要素及其余内容
@Data
@Entity
@Table(name = "tk_v1_teacher_performance_assessment")
public class TeacherPerformanceAssessment extends Model {

    // 唯一标识(最终ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    // 评价要素id集合(存储不同评价要素共享同一评价标准和得分时的id集合,以逗号分割) ()
    @Column(name = "element_ids")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String elementIds;
    @Transient
    public EvaluationElement evaluationElement;
    @Transient
    public List<EvaluationElement> evaluationElementList;//插入信息

    // 评价指标ID(对应的EvaluationIndicator)
    @Column(name="evaluation_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long evaluationId;

    //评价内容（含多个评分点）
    @Column(name = "content_ids")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String contentIds;
    @Transient
    public List<EvaluationContent> evaluationContentList;//插入信息

    // 评价标准  合格与不合格
    @Column(name = "evaluation_standard")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationStandard;

    // 得分
    @Column(name = "score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double score;

    public Boolean isBlock(){
        return this.elementIds.split(",").length >= 1;
    }

    public static Finder<Long, TeacherPerformanceAssessment> find =
            new Finder<>(TeacherPerformanceAssessment.class);

}
