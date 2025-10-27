package models.school.kpi.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.LocalDate;

/**
 * 考核明细实体 - 对应每个评价要素
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_assessment_detail")
public class AssessmentDetail extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "assessment_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long assessmentId;
    @Transient
    public TeacherPerformanceAssessment assessment;

    // 评价指标分类
    @Column(name = "category_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer categoryId;

    // 评价要素
    @Column(name = "evaluation_element", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationElement;

    // 评价内容
    @Column(name = "evaluation_content", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationContent;

    // 评价标准
    @Column(name = "evaluation_standard", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationStandard;

    // 得分
    @Column(name = "score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double score;

    // 最高分值
    @Column(name = "max_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double maxScore;

    // 评价证据/证明材料
    @Column(name = "evidence_description", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evidenceDescription;

    // 评价人
    @Column(name = "evaluator", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluator;

    // 评价时间
    @Column(name = "evaluate_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate evaluateDate;

    // 评价标准映射
    public AssessmentCategory getAssessmentCategory(){
        return switch (categoryId) {
            case 1 -> AssessmentCategory.MORAL_ETHICS;
            case 2 -> AssessmentCategory.TEACHING_ROUTINE;
            case 3 -> AssessmentCategory.TEACHING_ACHIEVEMENT;
            case 4 -> AssessmentCategory.PERSONAL_DEVELOPMENT;
            case 5 -> AssessmentCategory.CRITICAL_WORK;
            default -> null;
        };
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, AssessmentDetail> find =
            new Finder<>(AssessmentDetail.class);
}
