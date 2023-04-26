package com.autosale.shop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GenericSerializationUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static <T> byte[] serialize(T t)
    {
        return objectMapper.writeValueAsBytes(t);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] bytes, Class<T> tClass)
    {
        return objectMapper.readValue(bytes, tClass);
    }
}
