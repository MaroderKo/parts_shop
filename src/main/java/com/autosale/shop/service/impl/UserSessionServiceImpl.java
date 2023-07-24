package com.autosale.shop.service.impl;

import com.autosale.shop.model.User;
import com.autosale.shop.model.UserSession;
import com.autosale.shop.repository.UserSessionRepository;
import com.autosale.shop.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    //SOLID
    // L - Liskov Substitution principle
    // Об'єкт класу створюється з об'єктом інтерфейсу а не класу, що дозволяє змінювати реалізації певних інтерфейсів

    private final UserSessionRepository repository;

    @Override
    public UserSession createSession(User user) {
        UserSession session = new UserSession(UUID.randomUUID(), user);
        repository.create(user.getId().toString(), session);
        return session;
    }

    @Override
    public boolean terminateSession(int id) {
        return repository.delete(Integer.toString(id));
    }

    @Override
    public Optional<UserSession> getSession(User user) {
        return repository.read(user.getId().toString());
    }

}
