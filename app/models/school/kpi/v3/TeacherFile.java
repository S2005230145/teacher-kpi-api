package models.school.kpi.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.ebean.Finder;
import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import myannotation.EscapeHtmlSerializer;
import myannotation.StringToLongDeserializer;

/**
 * 内容附件
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tk_v3_teacher_file")
public class TeacherFile extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long id;

    @Column(name = "content_id")
    @JsonDeserialize(using = StringToLongDeserializer.class)
    public Long contentId;

    @Column(name = "description")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String description;

    @Column(name = "file_path")
    @JsonDeserialize(using = EscapeHtmlSerializer.class)
    public String filePath;

    public static Finder<Long, TeacherFile> find =
            new Finder<>(TeacherFile.class);
}
