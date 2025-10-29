package models.school.kpi.export;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

@Data
public class TeacherPerformanceExportRequest {

    @JsonDeserialize(using = StringToLongDeserializer.class)
    private Long kpiId;
    @JsonDeserialize(using = StringToLongDeserializer.class)
    private Long teacherId;
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    private String academicYear; // 学年
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    private String semester;     // 学期
    @Transient
    private ExportFormat format = ExportFormat.PDF;
    @Transient
    private ExportType exportType = ExportType.DETAILED;

    public enum ExportFormat {
        PDF, WORD
    }

    public enum ExportType {
        SUMMARY, DETAILED, BOTH
    }
}
