package com.autosale.shop.listeners;

import com.autosale.shop.model.UserSession;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

@Configuration
@RequiredArgsConstructor
public class RedisEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public RedisPubSubAdapter<String, String> redisKeyExpiredListener() {
        return new RedisPubSubAdapter<>() {
            @Override
            public void message(String pattern, String channel, String message) {
                System.out.println("Key expired: " + message);
                applicationEventPublisher.publishEvent(new RedisKeyExpiredEvent<UserSession>(message.getBytes()));
            }
        };
    }

    @Bean
    public StatefulRedisPubSubConnection<String, String> configureRedisKeyExpirationNotifications(RedisClient redisClient, RedisPubSubAdapter<String, String> listener) {
        StatefulRedisPubSubConnection<String, String> pubSubConnection = redisClient.connectPubSub();
        pubSubConnection.addListener(listener);
        RedisPubSubCommands<String, String> pubSubCommands = pubSubConnection.sync();
        pubSubCommands.psubscribe("__key*__:expired");
        return pubSubConnection;
    }
}
