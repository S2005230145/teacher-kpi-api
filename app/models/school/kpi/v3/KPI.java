package models.school.kpi.v3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.DoubleDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.EscapeHtmlSerializer;
import myannotation.StringToLongDeserializer;

import java.util.ArrayList;
import java.util.Date;
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

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date endTime;

    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    @Transient
    List<Indicator> indicatorList= new ArrayList<>();

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, KPI> find =
            new Finder<>(KPI.class);
}