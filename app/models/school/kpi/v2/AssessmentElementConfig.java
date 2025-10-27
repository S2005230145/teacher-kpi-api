package models.school.kpi.v2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;

/**
 * 评价要素配置实体 - 定义考核模板
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_assessment_element_config")
public class AssessmentElementConfig extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "category_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer categoryId;
    
    @Column(name = "element_name", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String elementName;

    @Column(name = "element_code", length = 50, unique = true)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String elementCode;

    @Column(name = "content_description", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String contentDescription;

    @Column(name = "evaluation_criteria", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationCriteria;

    @Column(name = "max_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double maxScore;

    @Column(name = "weight")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double weight;

    @Column(name = "calculation_method", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String calculationMethod;

    @Column(name = "is_active")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean isActive = true;

    @Column(name = "display_order")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer displayOrder;

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
    public static Finder<Long, AssessmentElementConfig> find =
            new Finder<>(AssessmentElementConfig.class);


}
