package models.school.kpi.param;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import models.school.kpi.v1.indicator.EvaluationContent;
import models.school.kpi.v1.indicator.EvaluationElement;
import myannotation.DoubleDeserializer;
import myannotation.EscapeHtmlAuthoritySerializer;

import java.util.List;

@Data
public class TPAParam {
    private List<EvaluationElement> evaluationElementList;

    private List<EvaluationContent> evaluationContentList;

    @JsonDeserialize(using = EscapeHtmlAuthoritySerializer.class)
    private String evaluationStandard;

    @JsonDeserialize(using = DoubleDeserializer.class)
    private Double score;
}
