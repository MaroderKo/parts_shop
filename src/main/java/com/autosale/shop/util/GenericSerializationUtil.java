package com.autosale.shop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;


public class GenericSerializationUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private GenericSerializationUtil() {
    }

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
