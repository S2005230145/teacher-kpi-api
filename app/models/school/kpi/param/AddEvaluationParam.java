package models.school.kpi.param;

import lombok.Data;
import models.school.kpi.v1.indicator.TeacherPerformanceAssessment;

import java.util.List;

@Data
public class AddEvaluationParam {

    private String indicatorName;

    private String indicatorSubName;

    private Long kpiId;//哪类kpiId(老师或学生)

    private List<TeacherPerformanceAssessment> TPAList;
}
