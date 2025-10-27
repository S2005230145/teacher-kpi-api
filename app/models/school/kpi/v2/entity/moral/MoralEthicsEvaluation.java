package models.school.kpi.v2.entity.moral;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.school.kpi.v2.TeacherPerformanceAssessment;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.LocalDate;

/**
 * 师德师风评价实体（一票否决项）
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "moral_ethics_evaluation")
public class MoralEthicsEvaluation extends Model {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "teacher_id", nullable = false)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long teacherId;

    @Column(name = "assessment_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long assessmentId;

    // 遵守职业行为准则情况
    @Column(name = "behavior_standard_compliance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean behaviorStandardCompliance;

    // 是否存在违反职业道德行为
    @Column(name = "has_violation_behavior")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean hasViolationBehavior;

    // 是否存在负面清单行为
    @Column(name = "has_negative_list_behavior")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean hasNegativeListBehavior;

    // 综合评价等级
    @Column(name = "evaluation_level_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer evaluationLevelId;

    // 评价依据
    @Column(name = "evaluation_basis", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationBasis;

    // 评价人
    @Column(name = "evaluator", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluator;

    // 评价日期
    @Column(name = "evaluate_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate evaluateDate;

    // 业务方法：判断是否合格
    public boolean isQualified() {
        return Boolean.TRUE.equals(behaviorStandardCompliance) &&
                !Boolean.TRUE.equals(hasViolationBehavior) &&
                !Boolean.TRUE.equals(hasNegativeListBehavior);
    }

    public void determineLevel() {
        if (!isQualified()) {
            this.evaluationLevelId = 3;
        } else {
            // 根据具体表现评定优/合格
            this.evaluationLevelId = 2;
        }
    }

    public MoralEthicsLevel getMoralEthicsLevel(){
        return switch (evaluationLevelId) {
            case 1 -> MoralEthicsLevel.EXCELLENT;
            case 2 -> MoralEthicsLevel.QUALIFIED;
            case 3 -> MoralEthicsLevel.UNQUALIFIED;
            default -> null;
        };
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, MoralEthicsEvaluation> find =
            new Finder<>(MoralEthicsEvaluation.class);
}
