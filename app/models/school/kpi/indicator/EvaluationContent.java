package models.school.kpi.indicator;

import io.ebean.Model;
import jakarta.persistence.*;

//评价内容
@Entity
@Table(name = "teacher_content")
public class EvaluationContent extends Model {
    // 唯一标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    // 评价内容
    @Column(name = "evaluation_content")
    public String evaluationContent;

    // 内容得分
    @Column(name = "score")
    public Double score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
