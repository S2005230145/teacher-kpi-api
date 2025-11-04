package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.DoubleDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

/**
 * 教师内容里当前要素合计分数
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_teacher_element_score")
public class TeacherElementScore extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "user_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long userId;

    @Column(name = "element_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long elementId;

    @Column(name = "kpi_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long kpiId;

    //该要数的初始总分
    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    //父级Id
    @Column(name = "task_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long taskId;

    //该要数的最终总分
    @Column(name = "final_score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double finalScore;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeacherElementScore> find =
            new Finder<>(TeacherElementScore.class);
}
