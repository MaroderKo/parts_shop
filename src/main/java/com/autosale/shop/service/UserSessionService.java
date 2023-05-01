package com.autosale.shop.service;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserSession;

import java.util.Optional;

public interface UserSessionService {
    UserSession createSession(User user);

    boolean terminateSession(int id);

    Optional<UserSession> getSession(User user);
}
