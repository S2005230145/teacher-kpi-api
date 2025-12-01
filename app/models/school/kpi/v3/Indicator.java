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

import java.util.List;

/**
 * 评价指标
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_indicator")
public class Indicator extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long id;

    @Column(name = "kpi_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long kpiId;

    //评价指标
    @Column(name = "indicator_name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String indicatorName;

    //评价子指标
    @Column(name = "sub_name")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String subName;

    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    @Transient
    List<Element> elementList;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, Indicator> find =
            new Finder<>(Indicator.class);
}
