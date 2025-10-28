package models.school.kpi.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import myannotation.EscapeHtmlAuthoritySerializer;
import myannotation.StringToLongDeserializer;

import java.util.List;

@Data
public class AddEvaluationParam {
    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    private String indicatorName;

    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    private String indicatorSubName;

    @JsonDeserialize(using = StringToLongDeserializer.class)
    private Long kpiId;//哪类kpiId(老师或学生)

    private List<TPAParam> tpalist;
}
