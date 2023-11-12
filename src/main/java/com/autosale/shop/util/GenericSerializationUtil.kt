package com.autosale.shop.util

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.SneakyThrows

object GenericSerializationUtil {
    private val objectMapper = ObjectMapper()
    @SneakyThrows
    fun <T> serialize(t: T): ByteArray {
        return objectMapper.writeValueAsBytes(t)
    }

    @SneakyThrows
    fun <T> deserialize(bytes: ByteArray, tClass: Class<T>): T {
        return objectMapper.readValue(bytes, tClass)
    }
}
