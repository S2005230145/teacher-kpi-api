package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

import java.util.List;

/**
 * 上级评分任务
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_teacher_task")
public class TeacherTask extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public Long id;

    @Column(name = "user_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long userId;

    @Transient
    public String userName;

    @Transient
    public String parentName;

    @Column(name = "parent_ids")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String parentIds;

    @Column(name = "status")
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    public String status;

    @Column(name = "tis_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long tisId;

    @Transient
    TeacherIndicatorScore teacherIndicatorScore;

    @Transient
    Indicator indicator;

    // JPA查询器（可选，与原代码保持一致）
    public static Finder<Long, TeacherTask> find =
            new Finder<>(TeacherTask.class);
}
