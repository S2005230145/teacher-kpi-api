package myannotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }

        // 转换为小写处理
        value = value.trim().toLowerCase();

        // 支持的真值
        if ("true".equals(value) || "1".equals(value) || "yes".equals(value) ||
                "y".equals(value) || "on".equals(value)) {
            return true;
        }

        // 支持的假值
        if ("false".equals(value) || "0".equals(value) || "no".equals(value) ||
                "n".equals(value) || "off".equals(value)) {
            return false;
        }

        // 无法解析时抛出异常或返回默认值
        throw ctxt.weirdStringException(value, Boolean.class,
                "值必须是 true/false, 1/0, yes/no, on/off 等格式");
    }
}
