package com.autosale.shop.repository;

import com.autosale.shop.model.UserSession;

public interface UserSessionRepository {

    void create(String key, UserSession session);

    UserSession read(String key);

    boolean delete(String key);
}
