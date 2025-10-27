package models.school.kpi.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import models.school.kpi.v1.indicator.EvaluationIndicator;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.time.LocalDate;
import java.util.List;

/**
 * 主绩效考核要点
 */
@Data
@Entity
@Table(name = "tk_v1_kpi")
public class KPI extends Model {

    // 唯一标识
    @Id
    @Column(name = "id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDate endTime;

    // 用户ID
    @Column(name = "user_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long userId;

    //上级用户ID
    @Column(name = "parent_id")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long parentId;

    // 所有评价指标
    @Transient
    public List<EvaluationIndicator> evaluationIndicatorList;

    public static Finder<Long, KPI> find =
            new Finder<>(KPI.class);

}
