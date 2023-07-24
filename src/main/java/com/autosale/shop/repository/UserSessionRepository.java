package com.autosale.shop.repository;

import com.autosale.shop.model.UserSession;

import java.util.Optional;

//Поліморфізм
//Дає змогу використовувати різні класи у їхній загальній формі (кілька реалізацій через інтерфейс, тощо)

public interface UserSessionRepository {

    void create(String key, UserSession session);

    Optional<UserSession> read(String key);

    boolean delete(String key);
}
