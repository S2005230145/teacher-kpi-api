package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;

/**
 * 评分标准
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_standard")
public class Standard extends Model {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    //标准名
    @Column(name = "name")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String name;

    //级别 (5最高 1最低)
    @Column(name = "level")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public Integer level;

    //左临界分
    @Column(name = "left_limit_score")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public Double leftLimitScore;

    //左符号
    @Column(name = "left_operator")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String leftOperator;

    //连接符 and or  （原神，启动）
    @Column(name = "op")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String op;

    //右临界分
    @Column(name = "right_limit_score")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public Double rightLimitScore;

    //右符号
    @Column(name = "right_operator")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String rightOperator;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, Standard> find =
            new Finder<>(Standard.class);
}
