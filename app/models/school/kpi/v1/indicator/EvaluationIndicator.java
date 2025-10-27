package models.school.kpi.v1.indicator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.util.List;

/**
 * 评价指标（如师德师风,教育教学常规这些）
 */
@Data
@Entity
@Table(name="tk_v1_evaluation_indicator")
public class EvaluationIndicator extends Model {

    //唯一标识
    @Id
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public long id;

    //评价指标名称
    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String name;

    //评价指标名称附属信息
    @Column(name = "sub_name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String subName;

    //父级KPI
    @Column(name = "kpi_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long kpiId;

    //评价要素
    @Transient
    public List<TeacherPerformanceAssessment> teacherPerformanceAssessmentList;

    public static Finder<Long, EvaluationIndicator> find =
            new Finder<>(EvaluationIndicator.class);

}
