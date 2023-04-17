package com.autosale.shop.listeners;

import com.autosale.shop.repository.LogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheListener {

    private final LogsRepository logsRepository;

    @EventListener
    public void onSessionExpireEvent(RedisKeyExpiredEvent<?> event) {
        logsRepository.log("Session of user " + new String(event.getId()).substring(1) + " is expired");
    }

}
