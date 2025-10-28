package models.school.kpi.v1.indicator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import myannotation.DoubleDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

//评价内容
@Data
@Entity
@Table(name = "tk_v1_teacher_content")
public class EvaluationContent extends Model {
    // 唯一标识（绑定TeacherPerformanceAssessment.contentIds）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long id;

    // 评价内容
    @Column(name = "evaluation_content")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String evaluationContent;

    // 内容得分
    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    public static Finder<Long, EvaluationContent> find =
            new Finder<>(EvaluationContent.class);
}
