package models.school.kpi;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import models.school.kpi.indicator.EvaluationIndicator;

import java.util.List;

@Entity
@Table(name = "kpi")
public class KPI extends Model {

    // 唯一标识
    @Id
    @Column(name = "id")
    public Long id;

    // 用户ID
    @Column(name = "user_id")
    public Long userId;

    //上级用户ID
    @Column(name = "parent_id")
    public Long parentId;

    // 所有评价指标
    @Transient
    public List<EvaluationIndicator> evaluationIndicatorList;

    public static Finder<Long, KPI> find =
            new Finder<>(KPI.class);

}
