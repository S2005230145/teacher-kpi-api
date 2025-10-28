package myannotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringToLongDeserializer extends JsonDeserializer<Long> {
    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            try {
                return Long.parseLong(p.getText().trim());
            } catch (NumberFormatException e) {
                throw ctxt.weirdStringException(p.getText(), Long.class,
                        "不是有效的Long数值");
            }
        }
        return p.getLongValue();
    }
}