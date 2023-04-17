package com.autosale.shop.service.impl;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements com.autosale.shop.service.UserSessionService {

    @Override
    @Cacheable(value = "sessions", key = "#user.id")
    public UserSession createSession(User user) {
        return new UserSession(UUID.randomUUID(), user);
    }

    @Override
    @CacheEvict(value = "sessions", key = "#id")
    public void terminateSession(int id) {
    }

    @Override
    @Cacheable(value = "sessions", key = "#user.id", unless = "#result == null")
    public UserSession getSession(User user) {
        return null;
    }

}
