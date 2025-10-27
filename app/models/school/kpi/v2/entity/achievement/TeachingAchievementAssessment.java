package models.school.kpi.v2.entity.achievement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.school.kpi.v2.TeacherPerformanceAssessment;
import myannotation.EscapeHtmlAuthoritySerializer;

/**
 * 教育教学业绩考核实体 - 30-45分
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "teaching_achievement_assessment")
public class TeachingAchievementAssessment extends Model {
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

    // 1. 教育教学业绩
    @Column(name = "student_academic_development")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String studentAcademicDevelopment; // 学生学业发展情况

    @Column(name = "exam_types", length = 200)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String examTypes; // 中高考、合格性考试、市质检、期末考试、质量监测等

    @Column(name = "average_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double averageScore; // 平均分

    @Column(name = "pass_rate")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double passRate; // 及格率

    @Column(name = "excellence_rate")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double excellenceRate; // 优秀率

    @Column(name = "score_improvement")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double scoreImprovement; // 成绩提升率

    @Column(name = "city_ranking")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer cityRanking; // 全市排名

    @Column(name = "school_ranking")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer schoolRanking; // 校内排名

    @Column(name = "academic_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double academicScore;

    // 2. 示范引领
    @Column(name = "mentorship_agreement_fulfillment")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean mentorshipAgreementFulfillment; // 履行师徒协议

    @Column(name = "guidance_effectiveness", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String guidanceEffectiveness; // 指导成效

    @Column(name = "research_group_work", length = 300)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String researchGroupWork; // 学科研训组工作

    @Column(name = "master_studio_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean masterStudioParticipation; // 名师工作室参与

    @Column(name = "training_instructor_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean trainingInstructorRole; // 学科培训班导师

    @Column(name = "rural_teaching_activities")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean ruralTeachingActivities; // 送教送培下乡活动

    @Column(name = "demonstration_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double demonstrationScore;

    // 3. 任教班级获奖
    @Column(name = "class_meeting_awards", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String classMeetingAwards; // 班（团、队）会活动获奖

    @Column(name = "team_activity_awards", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teamActivityAwards; // 团队活动获奖

    @Column(name = "single_competition_awards", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String singleCompetitionAwards; // 单项竞赛获奖

    @Column(name = "comprehensive_honors", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String comprehensiveHonors; // 综合性荣誉表彰

    @Column(name = "award_levels", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardLevels; // 获奖级别：校级/市级/省级/国家级

    @Column(name = "class_awards_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double classAwardsScore;

    // 4. 指导学生获奖情况
    @Column(name = "subject_competition_guidance", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String subjectCompetitionGuidance; // 学科竞赛指导

    @Column(name = "innovation_competition_guidance", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String innovationCompetitionGuidance; // 创新大赛指导

    @Column(name = "sports_league_guidance", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String sportsLeagueGuidance; // 体育联赛指导

    @Column(name = "art_performance_guidance", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String artPerformanceGuidance; // 艺术展演指导

    @Column(name = "other_competition_guidance", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String otherCompetitionGuidance; // 其他竞赛指导

    @Column(name = "student_awards_levels", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String studentAwardsLevels; // 获奖级别

    @Column(name = "student_guidance_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double studentGuidanceScore;

    // 5. 校本课程、综合实践活动
    @Column(name = "school_based_curriculum_development")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean schoolBasedCurriculumDevelopment; // 参与校本课程研发

    @Column(name = "research_learning_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean researchLearningGuidance; // 指导研究性学习

    @Column(name = "comprehensive_practice_organization")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean comprehensivePracticeOrganization; // 组织综合实践

    @Column(name = "social_practice_organization")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean socialPracticeOrganization; // 组织社会实践

    @Column(name = "club_activities_guidance")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean clubActivitiesGuidance; // 指导社团活动

    @Column(name = "extracurricular_tutor_role")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean extracurricularTutorRole; // 担任课外活动辅导员

    @Column(name = "school_activities_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double schoolActivitiesScore;

    // 6. 教学团队合作
    @Column(name = "teaching_research_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean teachingResearchParticipation; // 参与教研活动

    @Column(name = "collective_preparation_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean collectivePreparationParticipation; // 参与集备活动

    @Column(name = "resource_sharing")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean resourceSharing; // 教案、课件资源共享

    @Column(name = "post_training_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean postTrainingParticipation; // 参加岗位练兵

    @Column(name = "training_learning_participation")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean trainingLearningParticipation; // 参加培训学习

    @Column(name = "class_observation_completion")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean classObservationCompletion; // 完成听评课任务

    @Column(name = "team_cooperation_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double teamCooperationScore;

    // 7. 学生（家长）评价
    @Column(name = "student_satisfaction_rate")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double studentSatisfactionRate; // 学生满意率

    @Column(name = "parent_satisfaction_rate")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double parentSatisfactionRate; // 家长满意率

    @Column(name = "evaluation_participant_count")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer evaluationParticipantCount; // 参与评价人数

    @Column(name = "satisfaction_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double satisfactionScore;

    @Column(name = "total_teaching_achievement_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double totalTeachingAchievementScore;

    // 业务方法
    public void calculateTotalScore() {
        double total = 0.0;

        // 计算各分项得分
        calculateAcademicScore();
        calculateDemonstrationScore();
        calculateClassAwardsScore();
        calculateStudentGuidanceScore();
        calculateSchoolActivitiesScore();
        calculateTeamCooperationScore();
        calculateSatisfactionScore();

        if (academicScore != null) total += academicScore;
        if (demonstrationScore != null) total += demonstrationScore;
        if (classAwardsScore != null) total += classAwardsScore;
        if (studentGuidanceScore != null) total += studentGuidanceScore;
        if (schoolActivitiesScore != null) total += schoolActivitiesScore;
        if (teamCooperationScore != null) total += teamCooperationScore;
        if (satisfactionScore != null) total += satisfactionScore;

        this.totalTeachingAchievementScore = Math.min(total, 45.0);
    }

    private void calculateAcademicScore() {
        double score = 0.0;

        // 基础成绩评分
        if (averageScore != null) score += averageScore * 0.3;
        if (passRate != null) score += passRate * 100 * 0.25;
        if (excellenceRate != null) score += excellenceRate * 100 * 0.25;

        // 进步情况加分
        if (scoreImprovement != null && scoreImprovement > 0) {
            score += scoreImprovement * 10;
        }

        // 排名加分
        if (cityRanking != null && cityRanking <= 10) {
            score += (11 - cityRanking) * 0.5;
        }
        if (schoolRanking != null && schoolRanking <= 3) {
            score += (4 - schoolRanking) * 0.3;
        }

        this.academicScore = Math.min(score, 20.0);
    }

    private void calculateDemonstrationScore() {
        double score = 0.0;

        if (Boolean.TRUE.equals(mentorshipAgreementFulfillment)) score += 3.0;
        if (researchGroupWork != null && !researchGroupWork.isEmpty()) score += 2.0;
        if (Boolean.TRUE.equals(masterStudioParticipation)) score += 4.0;
        if (Boolean.TRUE.equals(trainingInstructorRole)) score += 3.0;
        if (Boolean.TRUE.equals(ruralTeachingActivities)) score += 2.0;

        this.demonstrationScore = Math.min(score, 12.0);
    }

    private void calculateClassAwardsScore() {
        double score = calculateAwardsScore(classMeetingAwards, awardLevels);
        this.classAwardsScore = Math.min(score, 8.0);
    }

    private void calculateStudentGuidanceScore() {
        double score = calculateAwardsScore(subjectCompetitionGuidance, studentAwardsLevels);
        this.studentGuidanceScore = Math.min(score, 10.0);
    }

    private double calculateAwardsScore(String awards, String levels) {
        if (awards == null || awards.isEmpty()) return 0.0;

        double score = 0.0;
        String[] awardArray = awards.split(",");

        for (String award : awardArray) {
            if (award.trim().isEmpty()) continue;

            // 根据获奖级别计算分数
            if (levels != null && levels.contains("国家级")) score += 3.0;
            else if (levels != null && levels.contains("省级")) score += 2.0;
            else if (levels != null && levels.contains("市级")) score += 1.5;
            else if (levels != null && levels.contains("校级")) score += 1.0;
            else score += 0.5;
        }

        return score;
    }

    private void calculateSchoolActivitiesScore() {
        double score = 0.0;

        if (Boolean.TRUE.equals(schoolBasedCurriculumDevelopment)) score += 2.0;
        if (Boolean.TRUE.equals(researchLearningGuidance)) score += 1.5;
        if (Boolean.TRUE.equals(comprehensivePracticeOrganization)) score += 1.5;
        if (Boolean.TRUE.equals(socialPracticeOrganization)) score += 1.5;
        if (Boolean.TRUE.equals(clubActivitiesGuidance)) score += 1.5;
        if (Boolean.TRUE.equals(extracurricularTutorRole)) score += 1.0;

        this.schoolActivitiesScore = Math.min(score, 8.0);
    }

    private void calculateTeamCooperationScore() {
        double score = 0.0;

        if (Boolean.TRUE.equals(teachingResearchParticipation)) score += 2.0;
        if (Boolean.TRUE.equals(collectivePreparationParticipation)) score += 2.0;
        if (Boolean.TRUE.equals(resourceSharing)) score += 1.5;
        if (Boolean.TRUE.equals(postTrainingParticipation)) score += 1.5;
        if (Boolean.TRUE.equals(trainingLearningParticipation)) score += 1.5;
        if (Boolean.TRUE.equals(classObservationCompletion)) score += 1.5;

        this.teamCooperationScore = Math.min(score, 10.0);
    }

    private void calculateSatisfactionScore() {
        double score = 0.0;

        if (studentSatisfactionRate != null) {
            score += studentSatisfactionRate * 0.5;
        }
        if (parentSatisfactionRate != null) {
            score += parentSatisfactionRate * 0.5;
        }

        this.satisfactionScore = Math.min(score * 10, 5.0);
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeachingAchievementAssessment> find =
            new Finder<>(TeachingAchievementAssessment.class);
}