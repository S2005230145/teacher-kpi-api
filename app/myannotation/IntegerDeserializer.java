package myannotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class IntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        if (node == null || node.isNull()) {
            return null;
        }

        // 处理字符串类型的数字
        if (node.isTextual()) {
            String text = node.asText().trim();
            if (text.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                throw new IOException("无法将 '" + text + "' 转换为 Integer", e);
            }
        }

        // 处理数字类型
        if (node.isNumber()) {
            return node.asInt();
        }

        // 处理布尔类型（true=1, false=0）
        if (node.isBoolean()) {
            return node.asBoolean() ? 1 : 0;
        }

        throw new IOException("无法将 " + node.getNodeType() + " 类型转换为 Integer");
    }
}
