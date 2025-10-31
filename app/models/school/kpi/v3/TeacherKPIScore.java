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

@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_teacher_kpi_score")
public class TeacherKPIScore extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "user_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long userId;

    @Column(name = "kpi_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long kpiId;

    //该KPI的总分
    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeacherKPIScore> find =
            new Finder<>(TeacherKPIScore.class);
}
