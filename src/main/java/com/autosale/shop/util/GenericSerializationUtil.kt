package com.autosale.shop.util

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.SneakyThrows

object GenericSerializationUtil {
    val objectMapper = ObjectMapper()
    @SneakyThrows
    fun <T> serialize(t: T): ByteArray {
        return objectMapper.writeValueAsBytes(t)
    }

    @SneakyThrows
    inline fun <reified T> deserialize(bytes: ByteArray): T {
        return objectMapper.readValue(bytes, T::class.java)
    }
}
