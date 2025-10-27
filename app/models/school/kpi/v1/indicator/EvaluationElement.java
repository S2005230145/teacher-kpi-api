package models.school.kpi.v1.indicator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;

//评价指标
@Data
@Entity
@Table(name="tk_v1_evaluation_element")
public class EvaluationElement extends Model {
    //唯一标识（绑定TeacherPerformanceAssessment.elementIds）
    @Id
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public long id;

    //评价要素名称
    @Column(name="name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String name;

    public static Finder<Long, EvaluationElement> find =
            new Finder<>(EvaluationElement.class);
}