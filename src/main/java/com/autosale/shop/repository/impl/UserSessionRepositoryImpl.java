package com.autosale.shop.repository.impl;

import com.autosale.shop.model.UserSession;
import com.autosale.shop.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSessionRepositoryImpl implements UserSessionRepository {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    private final GenericJackson2JsonRedisSerializer serializer;

    @Value("${jwt.access-token-lifetime}")
    int sessionTimeToLive;


    @Override
    public void create(String key, UserSession session) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            connection.stringCommands().setEx(key.getBytes(), sessionTimeToLive * 60L, serializer.serialize(session));
        }
    }

    @Override
    public UserSession read(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            byte[] bytes = connection.stringCommands().get(key.getBytes());
            if (bytes == null) {
                return null;
            }
            Object result = serializer.deserialize(bytes);
            if (result instanceof UserSession) {
                return (UserSession) result;
            }
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            return connection.commands().expireAt(key.getBytes(), 1);
        }
    }
}
