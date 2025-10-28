package myannotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DoubleDeserializer extends JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        // 处理字符串类型的数值
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            String value = p.getText().trim();
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw ctxt.weirdStringException(value, Double.class, "不是有效的数值");
            }
        }
        // 默认处理数值类型
        return (Double) p.getNumberValue();
    }
}
