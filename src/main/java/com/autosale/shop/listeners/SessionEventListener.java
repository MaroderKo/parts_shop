package com.autosale.shop.listeners;

import com.autosale.shop.service.TraceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEventListener {

    private final TraceHistoryService traceHistoryService;

    @EventListener
    public void onSessionExpireEvent(RedisKeyExpiredEvent<?> event) {
        traceHistoryService.log(String.format("Session of user %s is expired", new String(event.getId())));
    }

}
