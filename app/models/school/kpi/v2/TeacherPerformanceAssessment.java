package models.school.kpi.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.LocalDate;
import java.util.List;

/**
 * 教师绩效考核主实体
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_teacher_performance_assessment")
public class TeacherPerformanceAssessment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    //教师Id
    @Column(name = "teacher_id", nullable = false)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long teacherId;

    //学校年份
    @Column(name = "academic_year", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String academicYear;

    //接收时间
    @Column(name = "assessment_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate assessmentDate;

    //总分
    @Column(name = "total_score")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Double totalScore;

    //最终得分
    @Column(name = "final_grade", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String finalGrade;

    //评审人
    @Column(name = "assessor", length = 50)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String assessor;

    //评审状态
    @Column(name = "assessment_status", length = 20)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String assessmentStatus;

    // 关联的考核明细
    @Transient
    public List<AssessmentDetail> assessmentDetails;

    // 业务方法
    public void calculateTotalScore() {
        if (assessmentDetails != null) {
            this.totalScore = assessmentDetails.stream()
                    .mapToDouble(detail -> detail.getScore() != null ? detail.getScore() : 0.0)
                    .sum();
            determineFinalGrade();
        }
    }

    public void determineFinalGrade() {
        if (totalScore >= 90) this.finalGrade = "优秀";
        else if (totalScore >= 80) this.finalGrade = "良好";
        else if (totalScore >= 70) this.finalGrade = "合格";
        else this.finalGrade = "不合格";
    }

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeacherPerformanceAssessment> find =
            new Finder<>(TeacherPerformanceAssessment.class);
}
