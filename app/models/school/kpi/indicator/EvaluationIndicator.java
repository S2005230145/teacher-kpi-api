package models.school.kpi.indicator;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="evaluation_indicator")
public class EvaluationIndicator extends Model {

    //唯一标识
    @Id
    @Column(name = "id")
    public long id;

    //评价指标名称
    @Column(name = "name")
    public String name;

    //评价指标名称附属信息
    @Column(name = "sub_name")
    public String subName;

    //父级KPI
    @Column(name = "kpi_id")
    public Long kpiId;

    //评价要素
    @Transient
    public List<TeacherPerformanceAssessment> teacherPerformanceAssessmentList;


    public static Finder<Long, EvaluationIndicator> find =
            new Finder<>(EvaluationIndicator.class);

}
