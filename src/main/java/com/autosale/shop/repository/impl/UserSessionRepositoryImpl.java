package com.autosale.shop.repository.impl;

import com.autosale.shop.model.UserSession;
import com.autosale.shop.repository.UserSessionRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.autosale.shop.util.GenericSerializationUtil.deserialize;
import static com.autosale.shop.util.GenericSerializationUtil.serialize;

@Repository
@Slf4j
public class UserSessionRepositoryImpl implements UserSessionRepository {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    private final int sessionTimeToLive;

    public UserSessionRepositoryImpl(LettuceConnectionFactory lettuceConnectionFactory, @Value("${jwt.access-token-lifetime}") int sessionTimeToLive) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
        this.sessionTimeToLive = sessionTimeToLive;
    }

    @Override
    @SneakyThrows
    public void create(String key, UserSession session) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            connection.stringCommands().setEx(key.getBytes(), sessionTimeToLive * 60L, serialize(session));
        }
    }

    @Override
    @SneakyThrows
    public Optional<UserSession> read(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            byte[] bytes = connection.stringCommands().get(key.getBytes());
            return Optional.of(deserialize(bytes, UserSession.class));
        }
        catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(String key) {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            return connection.commands().expireAt(key.getBytes(), 1);
        }
    }
}
