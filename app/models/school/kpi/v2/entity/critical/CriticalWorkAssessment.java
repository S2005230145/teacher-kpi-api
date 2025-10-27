package models.school.kpi.v2.entity.critical;

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
 * 承担急难险重工作考核实体 - 5-10分
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "critical_work_assessment")
public class CriticalWorkAssessment extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "teacher_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long teacherId;

    @Column(name = "assessment_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long assessmentId;

    @Column(name = "academic_year", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String academicYear;

    // 1. 承担学校行政工作
    @Column(name = "administrative_positions", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String administrativePositions; // 行政职务

    @Column(name = "work_categories", length = 200)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String workCategories; // 工作类别：党建/教研/工会/后勤/安全/宣传/财务等

    @Column(name = "specific_duties", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String specificDuties; // 具体职务：集备组长/支部书记/党务秘书/安全专干/财务人员/实验组组长/处室干事等

    @Column(name = "work_performance", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String workPerformance; // 工作表现

    @Column(name = "work_difficulty_level", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String workDifficultyLevel; // 工作难度级别

    @Column(name = "work_importance_level", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String workImportanceLevel; // 工作重要程度

    @Column(name = "work_completion_evaluation", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String workCompletionEvaluation; // 工作完成情况评价

    @Column(name = "administrative_work_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double administrativeWorkScore;

    // 2. 承担学校临时性重要任务
    @Column(name = "temporary_task_types", length = 200)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String temporaryTaskTypes; // 任务类型：创建/评估/迎检/校庆/志愿服务等

    @Column(name = "task_descriptions", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String taskDescriptions; // 任务描述

    @Column(name = "task_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate taskStartDate;

    @Column(name = "task_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate taskEndDate;

    @Column(name = "task_urgency_level", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String taskUrgencyLevel; // 任务紧急程度

    @Column(name = "task_completion_status", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String taskCompletionStatus; // 任务完成状态

    @Column(name = "task_achievements", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String taskAchievements; // 任务成果

    @Column(name = "temporary_tasks_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double temporaryTasksScore;

    @Column(name = "total_critical_work_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double totalCriticalWorkScore;

    // 业务方法
    public void calculateTotalScore() {
        double total = 0.0;

        calculateAdministrativeWorkScore();
        calculateTemporaryTasksScore();

        if (administrativeWorkScore != null) total += administrativeWorkScore;
        if (temporaryTasksScore != null) total += temporaryTasksScore;

        this.totalCriticalWorkScore = Math.min(total, 10.0);
    }

    private void calculateAdministrativeWorkScore() {
        double score = 0.0;

        // 基础职务分
        if (specificDuties != null) {
            String[] duties = specificDuties.split(",");
            for (String duty : duties) {
                if (duty.trim().isEmpty()) continue;
                score += calculateDutyBaseScore(duty.trim());
            }
        }

        // 工作表现加分
        if ("优秀".equals(workCompletionEvaluation)) score += 2.0;
        else if ("良好".equals(workCompletionEvaluation)) score += 1.5;
        else if ("合格".equals(workCompletionEvaluation)) score += 1.0;

        // 工作难度和重要性加分
        if ("高".equals(workDifficultyLevel)) score += 1.5;
        else if ("中".equals(workDifficultyLevel)) score += 1.0;
        else if ("低".equals(workDifficultyLevel)) score += 0.5;

        if ("重要".equals(workImportanceLevel)) score += 1.5;
        else if ("一般".equals(workImportanceLevel)) score += 1.0;

        this.administrativeWorkScore = Math.min(score, 7.0);
    }

    private double calculateDutyBaseScore(String duty) {
        return switch (duty) {
            case "支部书记", "党务秘书", "安全专干", "财务人员" -> 2.0;
            case "集备组长", "实验组组长", "处室干事" -> 1.5;
            default -> 1.0;
        };
    }

    private void calculateTemporaryTasksScore() {
        double score = 0.0;

        if (temporaryTaskTypes != null) {
            String[] tasks = temporaryTaskTypes.split(",");
            for (String task : tasks) {
                if (task.trim().isEmpty()) continue;
                score += calculateTaskBaseScore(task.trim());
            }
        }

        // 任务完成情况加分
        if ("优秀".equals(taskCompletionStatus)) score += 1.5;
        else if ("良好".equals(taskCompletionStatus)) score += 1.0;
        else if ("合格".equals(taskCompletionStatus)) score += 0.5;

        // 紧急程度加分
        if ("紧急".equals(taskUrgencyLevel)) score += 1.0;
        else if ("一般".equals(taskUrgencyLevel)) score += 0.5;

        this.temporaryTasksScore = Math.min(score, 5.0);
    }

    private double calculateTaskBaseScore(String task) {
        return switch (task) {
            case "创建", "评估", "迎检" -> 1.5;
            case "校庆", "志愿服务" -> 1.0;
            default -> 0.8;
        };
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, CriticalWorkAssessment> find =
            new Finder<>(CriticalWorkAssessment.class);
}
