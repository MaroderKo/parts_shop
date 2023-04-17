package com.autosale.shop.service;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserSession;

public interface UserSessionService {
    UserSession createSession(User user);

    void terminateSession(int id);

    UserSession getSession(User user);
}
