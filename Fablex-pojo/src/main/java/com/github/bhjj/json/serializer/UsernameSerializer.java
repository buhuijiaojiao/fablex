package com.github.bhjj.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 用户名序列化器（敏感信息，不应该在页面上完全显示）
 *
 * @author ZhangXianDuo
 * @date 2025/4/15
 */
public class UsernameSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        //将用户名第4到第7个字符转化为****
        jsonGenerator.writeString(s.substring(0, 4) + "****" + s.substring(8));

    }
}
