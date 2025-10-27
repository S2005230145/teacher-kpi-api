package models.school.kpi.indicator;

import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//评价指标
@Entity
@Table(name="evaluation_element")
public class EvaluationElement extends Model {
    //唯一标识
    @Id
    @Column(name = "id")
    public long id;

    //评价要素名称
    @Column(name="name")
    public String name;

    public static Finder<Long, EvaluationElement> find =
            new Finder<>(EvaluationElement.class);
}