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
 * 教师单个内容里的分数
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_teacher_content_score")
public class TeacherContentScore extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "user_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long userId;

    //唯一性
    @Column(name = "content_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long contentId;

    @Column(name = "element_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long elementId;

    //对应要素内容的分数
    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeacherContentScore> find =
            new Finder<>(TeacherContentScore.class);

}
