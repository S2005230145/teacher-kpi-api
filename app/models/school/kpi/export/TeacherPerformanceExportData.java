package models.school.kpi.export;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TeacherPerformanceExportData {

    // 基本信息
    private String schoolName = "福清市中小学/中职学校";
    private String teacherName;
    private String academicYear;
    private String semester;
    private String exportDate;

    // 第一部分：师德师风（定性评价）
    private MoralEvaluation moralEvaluation;

    // 第二部分：教育教学业绩（30-45分）
    private TeachingPerformance teachingPerformance;

    // 第三部分：个人专业发展（5-15分）
    private ProfessionalDevelopment professionalDevelopment;

    // 第四部分：承担急难险重工作（5-10分）
    private SpecialAssignments specialAssignments;

    // 总分
    private BigDecimal totalScore;
    private String evaluationLevel; // 优秀、合格、不合格

    @Data
    public static class MoralEvaluation {
        private String evaluationResult; // 优秀、合格、不合格
        private boolean followTenGuidelines; // 是否遵守十项准则
        private boolean noViolation; // 无违反职业道德行为
        private boolean noNegativeBehavior; // 无负面清单行为
        private String comments; // 评语
    }

    @Data
    public static class TeachingPerformance {
        private BigDecimal studentAcademicDevelopment; // 学生学业发展
        private BigDecimal demonstrationLeadership;    // 示范引领
        private BigDecimal classAwards;               // 班级获奖
        private BigDecimal studentGuidanceAwards;     // 指导学生获奖
        private BigDecimal schoolBasedActivities;     // 校本课程/实践活动
        private BigDecimal teachingTeamCollaboration;  // 教学团队合作
        private BigDecimal studentParentEvaluation;    // 学生家长评价

        // 教育教学常规（30-40分）
        private TeachingRoutine teachingRoutine;

        @Data
        public static class TeachingRoutine {
            private BigDecimal attendance;           // 出勤情况
            private BigDecimal teachingWorkload;      // 课时工作量
            private BigDecimal classTeacherWork;      // 班主任等工作
            private BigDecimal classroomTeaching;    // 课堂教学
            private BigDecimal studentDevelopment;   // 学生发展指导
            private BigDecimal homeSchoolConnection; // 家校联系
        }
    }

    @Data
    public static class ProfessionalDevelopment {
        private BigDecimal publicTeachingActivities; // 承担公开教学活动
        private BigDecimal teachingPapers;           // 撰写教育教学论文
        private BigDecimal teachingResearch;         // 参与教育教学研究
        private BigDecimal personalAwards;           // 个人获奖
    }

    @Data
    public static class SpecialAssignments {
        private BigDecimal administrativeWork;        // 承担学校行政工作
        private BigDecimal temporaryImportantTasks;  // 承担学校临时性重要任务
    }
}