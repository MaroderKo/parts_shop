package com.autosale.shop.repository.impl;

import com.autosale.shop.model.UserSession;
import com.autosale.shop.repository.UserSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserSessionRepositoryImpl implements UserSessionRepository {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    private final ObjectMapper serializer;

    private final int sessionTimeToLive;

    public UserSessionRepositoryImpl(LettuceConnectionFactory lettuceConnectionFactory, ObjectMapper serializer, @Value("${jwt.access-token-lifetime}") int sessionTimeToLive) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
        this.serializer = serializer;
        this.sessionTimeToLive = sessionTimeToLive;
    }

    @Override
    @SneakyThrows
    public void create(String key, UserSession session) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            connection.stringCommands().setEx(key.getBytes(), sessionTimeToLive * 60L, serializer.writeValueAsBytes(session));
        }
    }

    @Override
    @SneakyThrows
    public Optional<UserSession> read(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            byte[] bytes = connection.stringCommands().get(key.getBytes());
            if (bytes == null) {
                return Optional.empty();
            }
            return Optional.of(serializer.readValue(bytes, UserSession.class));
        }
    }

    @Override
    public boolean delete(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            return connection.commands().expireAt(key.getBytes(), 1);
        }
    }
}
