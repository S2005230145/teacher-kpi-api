package models.school.kpi.v2.entity.personal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.LocalDate;

/**
 * 个人专业发展考核实体 - 5-15分
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "personal_development_assessment")
public class PersonalDevelopmentAssessment extends Model {
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

    // 1. 承担公开教学活动
    @Column(name = "demonstration_classes_count")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer demonstrationClassesCount; // 示范课次数

    @Column(name = "observation_classes_count")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer observationClassesCount; // 观摩课次数

    @Column(name = "special_lectures_count")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer specialLecturesCount; // 专题讲座次数

    @Column(name = "class_observation_activities")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer classObservationActivities; // 班团队观摩活动

    @Column(name = "regional_exchange_activities")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer regionalExchangeActivities; // 区域交流活动

    @Column(name = "activity_levels", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String activityLevels; // 活动级别：校级/市级/省级/国家级

    @Column(name = "public_teaching_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double publicTeachingScore;

    // 2. 撰写教育教学论文
    @Column(name = "education_papers_count")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer educationPapersCount; // 论文数量

    @Column(name = "paper_titles", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String paperTitles; // 论文题目

    @Column(name = "paper_types", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String paperTypes; // 学科/德育/党建/管理论文

    @Column(name = "award_status", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardStatus; // 获奖状态

    @Column(name = "compilation_level", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String compilationLevel; // 汇编级别

    @Column(name = "publish_date")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public LocalDate publishDate;

    @Column(name = "papers_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double papersScore;

    // 3. 参与教育教学研究
    @Column(name = "research_projects", columnDefinition = "TEXT")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String researchProjects; // 研究课题

    @Column(name = "research_levels", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String researchLevels; // 研究级别

    @Column(name = "research_roles", length = 100)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String researchRoles; // 研究角色：主持/参与

    @Column(name = "subject_proposition_work")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean subjectPropositionWork; // 学科命题工作

    @Column(name = "continuing_education_completion")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Boolean continuingEducationCompletion; // 完成继续教育

    @Column(name = "continuing_education_hours")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Integer continuingEducationHours; // 继续教育学时

    @Column(name = "research_participation_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double researchParticipationScore;

    // 4. 个人获奖
    @Column(name = "teaching_special_awards", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teachingSpecialAwards; // 教育教学专项表彰

    @Column(name = "comprehensive_honors", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String comprehensiveHonors; // 综合性荣誉表彰

    @Column(name = "teaching_competition_awards", length = 500)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String teachingCompetitionAwards; // 公开教学竞赛获奖

    @Column(name = "excellent_teacher_certification")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String excellentTeacherCertification; // 名优骨干认定

    @Column(name = "award_dates")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String awardDates; // 获奖日期

    @Column(name = "personal_awards_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double personalAwardsScore;

    @Column(name = "total_personal_development_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double totalPersonalDevelopmentScore;

    // 业务方法
    public void calculateTotalScore() {
        double total = 0.0;

        calculatePublicTeachingScore();
        calculatePapersScore();
        calculateResearchParticipationScore();
        calculatePersonalAwardsScore();

        if (publicTeachingScore != null) total += publicTeachingScore;
        if (papersScore != null) total += papersScore;
        if (researchParticipationScore != null) total += researchParticipationScore;
        if (personalAwardsScore != null) total += personalAwardsScore;

        this.totalPersonalDevelopmentScore = Math.min(total, 15.0);
    }

    private void calculatePublicTeachingScore() {
        double score = 0.0;

        if (demonstrationClassesCount != null) score += demonstrationClassesCount * 0.8;
        if (observationClassesCount != null) score += observationClassesCount * 0.5;
        if (specialLecturesCount != null) score += specialLecturesCount * 1.0;
        if (regionalExchangeActivities != null) score += regionalExchangeActivities * 1.2;

        // 活动级别加分
        if (activityLevels != null) {
            if (activityLevels.contains("国家级")) score += 2.0;
            else if (activityLevels.contains("省级")) score += 1.5;
            else if (activityLevels.contains("市级")) score += 1.0;
            else if (activityLevels.contains("校级")) score += 0.5;
        }

        this.publicTeachingScore = Math.min(score, 6.0);
    }

    private void calculatePapersScore() {
        double score = 0.0;

        if (educationPapersCount != null) {
            score += educationPapersCount * 0.5;
        }

        // 获奖和汇编加分
        if ("获奖".equals(awardStatus)) {
            if (compilationLevel != null && compilationLevel.contains("国家级")) score += 3.0;
            else if (compilationLevel != null && compilationLevel.contains("省级")) score += 2.0;
            else if (compilationLevel != null && compilationLevel.contains("市级")) score += 1.5;
            else if (compilationLevel != null && compilationLevel.contains("校级")) score += 1.0;
        } else if ("汇编".equals(compilationLevel)) {
            score += 0.5;
        }

        this.papersScore = Math.min(score, 4.0);
    }

    private void calculateResearchParticipationScore() {
        double score = 0.0;

        if (researchProjects != null && !researchProjects.isEmpty()) {
            // 根据研究级别和角色计算分数
            if (researchLevels != null) {
                double baseScore = 0.0;
                if (researchLevels.contains("国家级")) baseScore = 3.0;
                else if (researchLevels.contains("省级")) baseScore = 2.0;
                else if (researchLevels.contains("市级")) baseScore = 1.5;
                else if (researchLevels.contains("校级")) baseScore = 1.0;

                // 角色权重
                if (researchRoles != null && researchRoles.contains("主持")) {
                    score += baseScore * 1.2;
                } else {
                    score += baseScore * 0.8;
                }
            }
        }

        if (Boolean.TRUE.equals(subjectPropositionWork)) score += 1.0;
        if (Boolean.TRUE.equals(continuingEducationCompletion)) score += 0.5;

        this.researchParticipationScore = Math.min(score, 5.0);
    }

    private void calculatePersonalAwardsScore() {
        double score = 0.0;

        // 根据获奖内容计算分数
        if (teachingSpecialAwards != null && !teachingSpecialAwards.isEmpty()) {
            score += calculateAwardScore(teachingSpecialAwards);
        }
        if (comprehensiveHonors != null && !comprehensiveHonors.isEmpty()) {
            score += calculateAwardScore(comprehensiveHonors);
        }
        if (teachingCompetitionAwards != null && !teachingCompetitionAwards.isEmpty()) {
            score += calculateAwardScore(teachingCompetitionAwards);
        }
        if (excellentTeacherCertification != null && !excellentTeacherCertification.isEmpty()) {
            score += 2.0; // 名优骨干认定固定加分
        }

        this.personalAwardsScore = Math.min(score, 5.0);
    }

    private double calculateAwardScore(String awards) {
        if (awards == null || awards.isEmpty()) return 0.0;

        double score = 0.0;
        if (awards.contains("国家级")) score += 3.0;
        else if (awards.contains("省级")) score += 2.0;
        else if (awards.contains("市级")) score += 1.5;
        else if (awards.contains("校级")) score += 1.0;
        else score += 0.5;

        return score;
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, PersonalDevelopmentAssessment> find =
            new Finder<>(PersonalDevelopmentAssessment.class);
}