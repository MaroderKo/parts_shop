package com.autosale.shop.repository;

import com.autosale.shop.model.UserSession;

import java.util.Optional;

public interface UserSessionRepository {

    void create(String key, UserSession session);

    Optional<UserSession> read(String key);

    boolean delete(String key);
}
