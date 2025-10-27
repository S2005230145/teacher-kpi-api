package models.school.kpi.v2.entity.custom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.school.kpi.v2.AssessmentElementConfig;
import myannotation.EscapeHtmlAuthoritySerializer;

/**
 * 教育教学常规考核实体 - 对应第一张图片中的30-40分部分
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "teaching_routine_assessment")
public class TeachingRoutineAssessment extends Model {
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

    // 1. 出勤情况
    @Column(name = "sick_leave_days")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer sickLeaveDays; // 病假天数

    @Column(name = "personal_leave_days")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer personalLeaveDays; // 事假天数

    @Column(name = "late_times")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer lateTimes; // 迟到次数

    @Column(name = "absence_times")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer absenceTimes; // 旷课次数

    @Column(name = "political_study_attendance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean politicalStudyAttendance; // 政治学习出勤

    @Column(name = "teaching_research_attendance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean teachingResearchAttendance; // 教研活动出勤

    @Column(name = "school_meeting_attendance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean schoolMeetingAttendance; // 学校会议出勤

    @Column(name = "other_collective_activities_attendance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean otherCollectiveActivitiesAttendance; // 其他集体活动出勤

    @Column(name = "attendance_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double attendanceScore;

    // 2. 课时工作量
    @Column(name = "standard_class_hours")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer standardClassHours; // 标准课时工作量

    @Column(name = "actual_class_hours")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer actualClassHours; // 实际完成课时

    @Column(name = "substitution_hours")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer substitutionHours; // 代课时数

    @Column(name = "special_class_hours")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer specialClassHours; // 特殊课时

    @Column(name = "workload_completion_rate")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double workloadCompletionRate; // 工作量完成率

    @Column(name = "workload_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double workloadScore;

    // 3. 班主任等工作
    @Column(name = "class_teacher_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean classTeacherRole; // 担任班主任

    @Column(name = "grade_leader_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean gradeLeaderRole; // 担任年段长

    @Column(name = "teaching_research_leader_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean teachingResearchLeaderRole; // 担任教研组长

    @Column(name = "young_pioneer_leader_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean youngPioneerLeaderRole; // 少先队总辅导员

    @Column(name = "youth_league_secretary_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean youthLeagueSecretaryRole; // 团委书记

    @Column(name = "middle_manager_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean middleManagerRole; // 中层以上干部

    @Column(name = "management_position_description", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String managementPositionDescription; // 管理职务描述

    @Column(name = "management_work_evaluation", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String managementWorkEvaluation; // 管理工作评价

    @Column(name = "management_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double managementScore;

    // 4. 课堂教学
    @Column(name = "teaching_plan_quality")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teachingPlanQuality; // 教学计划制定质量

    @Column(name = "teaching_organization")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teachingOrganization; // 教学组织情况

    @Column(name = "classroom_management")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String classroomManagement; // 课堂管理

    @Column(name = "teaching_concept")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teachingConcept; // 教学理念

    @Column(name = "classroom_effectiveness")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String classroomEffectiveness; // 课堂实效

    @Column(name = "moral_education_integration")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String moralEducationIntegration; // 德育渗透

    @Column(name = "modern_education_tech_integration")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String modernEducationTechIntegration; // 现代教育技术手段与学科融合

    @Column(name = "classroom_teaching_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double classroomTeachingScore;

    // 5. 学生发展指导
    @Column(name = "homework_correction_quality")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String homeworkCorrectionQuality; // 作业批改质量

    @Column(name = "personalized_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String personalizedGuidance; // 个性化辅导

    @Column(name = "psychological_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String psychologicalGuidance; // 心理辅导

    @Column(name = "student_comprehensive_assessment")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String studentComprehensiveAssessment; // 学生综合素质评定

    @Column(name = "growth_records_maintenance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String growthRecordsMaintenance; // 成长档案记录

    @Column(name = "learning_difficulty_support")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String learningDifficultySupport; // 帮扶学困生（特殊生）

    @Column(name = "career_planning_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String careerPlanningGuidance; // 生涯规划指导

    @Column(name = "five_education_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String fiveEducationGuidance; // 五育并举指导

    @Column(name = "learning_behavior_habits_cultivation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String learningBehaviorHabitsCultivation; // 学习行为习惯培养

    @Column(name = "student_development_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double studentDevelopmentScore;

    // 6. 家校联系
    @Column(name = "parent_meeting_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String parentMeetingParticipation; // 家长会参与情况

    @Column(name = "home_visit_records")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String homeVisitRecords; // 家访记录

    @Column(name = "parenting_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String parentingGuidance; // 家教指导

    @Column(name = "parent_school_training")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String parentSchoolTraining; // 家长学校培训

    @Column(name = "parent_contact_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double parentContactScore;

    @Column(name = "total_teaching_routine_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double totalTeachingRoutineScore;

    // 业务方法
    public void calculateTotalScore() {
        double total = 0.0;

        calculateAttendanceScore();
        calculateWorkloadScore();
        calculateManagementScore();
        calculateClassroomTeachingScore();
        calculateStudentDevelopmentScore();
        calculateParentContactScore();

        if (attendanceScore != null) total += attendanceScore;
        if (workloadScore != null) total += workloadScore;
        if (managementScore != null) total += managementScore;
        if (classroomTeachingScore != null) total += classroomTeachingScore;
        if (studentDevelopmentScore != null) total += studentDevelopmentScore;
        if (parentContactScore != null) total += parentContactScore;

        this.totalTeachingRoutineScore = Math.min(total, 40.0);
    }

    private void calculateAttendanceScore() {
        double score = 10.0; // 基础分10分

        // 扣分规则
        if (sickLeaveDays != null) score -= sickLeaveDays * 0.2;
        if (personalLeaveDays != null) score -= personalLeaveDays * 0.5;
        if (lateTimes != null) score -= lateTimes * 0.5;
        if (absenceTimes != null) score -= absenceTimes * 2.0;

        // 集体活动出勤扣分
        if (Boolean.FALSE.equals(politicalStudyAttendance)) score -= 1.0;
        if (Boolean.FALSE.equals(teachingResearchAttendance)) score -= 1.0;
        if (Boolean.FALSE.equals(schoolMeetingAttendance)) score -= 1.0;
        if (Boolean.FALSE.equals(otherCollectiveActivitiesAttendance)) score -= 1.0;

        this.attendanceScore = Math.max(score, 0.0);
    }

    private void calculateWorkloadScore() {
        double score = 0.0;

        // 标准课时完成率
        if (standardClassHours != null && standardClassHours > 0) {
            this.workloadCompletionRate = (double) actualClassHours / standardClassHours;
            score += Math.min(workloadCompletionRate, 1.0) * 8.0; // 基础分8分
        }

        // 代课加分
        if (substitutionHours != null) {
            score += Math.min(substitutionHours * 0.1, 1.0);
        }

        // 特殊课时加分
        if (specialClassHours != null) {
            score += Math.min(specialClassHours * 0.2, 1.0);
        }

        this.workloadScore = Math.min(score, 10.0);
    }

    private void calculateManagementScore() {
        double score = 0.0;

        // 不同职务的基础分
        if (Boolean.TRUE.equals(classTeacherRole)) score += 6.0;
        if (Boolean.TRUE.equals(gradeLeaderRole)) score += 5.0;
        if (Boolean.TRUE.equals(teachingResearchLeaderRole)) score += 4.0;
        if (Boolean.TRUE.equals(youngPioneerLeaderRole)) score += 4.0;
        if (Boolean.TRUE.equals(youthLeagueSecretaryRole)) score += 4.0;
        if (Boolean.TRUE.equals(middleManagerRole)) score += 5.5;

        // 工作评价加分
        if (managementWorkEvaluation != null) {
            if (managementWorkEvaluation.contains("优秀")) score += 2.0;
            else if (managementWorkEvaluation.contains("良好")) score += 1.5;
            else if (managementWorkEvaluation.contains("合格")) score += 1.0;
        }

        this.managementScore = Math.min(score, 10.0);
    }

    private void calculateClassroomTeachingScore() {
        double score = 0.0;

        // 教学计划制定（10%）
        if (teachingPlanQuality != null) {
            if (teachingPlanQuality.contains("优秀")) score += 1.0;
            else if (teachingPlanQuality.contains("良好")) score += 0.8;
            else if (teachingPlanQuality.contains("合格")) score += 0.6;
        }

        // 教学组织与课堂管理（30%）
        double organizationScore = 0.0;
        int orgCount = 0;
        if (teachingOrganization != null) {
            organizationScore += getQualityScore(teachingOrganization);
            orgCount++;
        }
        if (classroomManagement != null) {
            organizationScore += getQualityScore(classroomManagement);
            orgCount++;
        }
        if (orgCount > 0) score += (organizationScore / orgCount) * 3.0;

        // 教学理念与课堂实效（40%）
        double effectivenessScore = 0.0;
        int effCount = 0;
        if (teachingConcept != null) {
            effectivenessScore += getQualityScore(teachingConcept);
            effCount++;
        }
        if (classroomEffectiveness != null) {
            effectivenessScore += getQualityScore(classroomEffectiveness);
            effCount++;
        }
        if (effCount > 0) score += (effectivenessScore / effCount) * 4.0;

        // 德育渗透（10%）
        if (moralEducationIntegration != null) {
            score += getQualityScore(moralEducationIntegration) * 1.0;
        }

        // 现代教育技术应用（10%）
        if (modernEducationTechIntegration != null) {
            score += getQualityScore(modernEducationTechIntegration) * 1.0;
        }

        this.classroomTeachingScore = Math.min(score, 10.0);
    }

    private double getQualityScore(String quality) {
        if (quality.contains("优秀")) return 1.0;
        else if (quality.contains("良好")) return 0.8;
        else if (quality.contains("合格")) return 0.6;
        else return 0.4;
    }

    private void calculateStudentDevelopmentScore() {
        double score = 0.0;

        // 作业批改质量（20%）
        if (homeworkCorrectionQuality != null) {
            score += getQualityScore(homeworkCorrectionQuality) * 2.0;
        }

        // 个性化辅导（20%）
        if (personalizedGuidance != null && !personalizedGuidance.isEmpty()) {
            score += 2.0;
        }

        // 心理辅导（15%）
        if (psychologicalGuidance != null && !psychologicalGuidance.isEmpty()) {
            score += 1.5;
        }

        // 学生综合素质评定（15%）
        if (studentComprehensiveAssessment != null && !studentComprehensiveAssessment.isEmpty()) {
            score += 1.5;
        }

        // 成长档案记录（10%）
        if (growthRecordsMaintenance != null && !growthRecordsMaintenance.isEmpty()) {
            score += 1.0;
        }

        // 学困生帮扶（10%）
        if (learningDifficultySupport != null && !learningDifficultySupport.isEmpty()) {
            score += 1.0;
        }

        // 五育并举指导（10%）
        if (fiveEducationGuidance != null && !fiveEducationGuidance.isEmpty()) {
            score += 1.0;
        }

        this.studentDevelopmentScore = Math.min(score, 10.0);
    }

    private void calculateParentContactScore() {
        double score = 0.0;

        // 家长会参与（30%）
        if (parentMeetingParticipation != null && !parentMeetingParticipation.isEmpty()) {
            score += 3.0;
        }

        // 家访记录（30%）
        if (homeVisitRecords != null && !homeVisitRecords.isEmpty()) {
            score += 3.0;
        }

        // 家教指导（20%）
        if (parentingGuidance != null && !parentingGuidance.isEmpty()) {
            score += 2.0;
        }

        // 家长学校培训（20%）
        if (parentSchoolTraining != null && !parentSchoolTraining.isEmpty()) {
            score += 2.0;
        }

        this.parentContactScore = Math.min(score, 10.0);
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeachingRoutineAssessment> find =
            new Finder<>(TeachingRoutineAssessment.class);
}
