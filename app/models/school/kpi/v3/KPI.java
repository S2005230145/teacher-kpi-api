package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.EscapeHtmlSerializer;
import myannotation.StringToLongDeserializer;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_kpi")
public class KPI extends Model {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "title")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String title;

    @Transient
    List<Indicator> indicatorList= new ArrayList<>();

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, KPI> find =
            new Finder<>(KPI.class);
}