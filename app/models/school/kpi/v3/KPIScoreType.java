package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_kpi_score_type")
public class KPIScoreType extends Model {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;

    @Column(name = "json_param")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String jsonParam;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, KPIScoreType> find =
            new Finder<>(KPIScoreType.class);
}
