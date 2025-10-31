package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.BooleanDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

import java.util.List;

/**
 * 评价要素
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_element")
public class Element extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "indicator_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long indicatorId;

    //评价要素
    @Column(name = "element")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String element;

    //评价内容
    @Transient
    List<Content> contentList;

    @Column(name = "criteria")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String criteria;

    //是否系统自动评价
    @Column(name = "is_auto")
    @JsonDeserialize(using = BooleanDeserializer.class)
    public Boolean isAuto;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, Element> find =
            new Finder<>(Element.class);
}
