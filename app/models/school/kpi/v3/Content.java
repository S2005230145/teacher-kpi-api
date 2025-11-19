package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.DoubleDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.IntegerDeserializer;
import myannotation.StringToLongDeserializer;

/**
 * 评价内容
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_content")
public class Content extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long id;

    @Column(name = "element_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long elementId;

    //评价内容 共享一个标准时使用 @#$ 分割  否则用 | 分割
    @Column(name = "content")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String content;

    //每次分数
    @Column(name = "score")
    @JsonDeserialize(using = DoubleDeserializer.class)
    public Double score;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, Content> find =
            new Finder<>(Content.class);
}
