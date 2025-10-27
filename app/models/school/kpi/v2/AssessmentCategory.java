package models.school.kpi.v2;

import lombok.Getter;

/**
 * 评价指标分类枚举
 */
@Getter
public enum AssessmentCategory {
    MORAL_ETHICS("师德师风", "师德师风评价，一票否决"),
    TEACHING_ROUTINE("教育教学常规", "教学常规工作，30-40分"),
    TEACHING_ACHIEVEMENT("教育教学业绩", "教学成果业绩，30-45分"),
    PERSONAL_DEVELOPMENT("个人专业发展", "教师专业成长，5-15分"),
    CRITICAL_WORK("承担急难险重工作", "特殊工作任务，5-10分");


    private final String name;
    private final String description;

    AssessmentCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
