package com.autosale.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisObjectMappingConfiguration {

    @Bean
    GenericJackson2JsonRedisSerializer userSessionRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
