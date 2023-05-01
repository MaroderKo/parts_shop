package com.autosale.shop.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConnectionConfiguration {

    private final String host;
    private final int port;

    public RedisConnectionConfiguration(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration localhost = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(localhost);
    }

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.create(host, port));
    }
}
