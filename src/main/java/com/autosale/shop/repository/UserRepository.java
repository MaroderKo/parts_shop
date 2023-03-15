package com.autosale.shop.repository;

import com.autosale.shop.model.User;
import org.jooq.Record1;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(int id);

    Optional<Integer> save(User user);

    void update(User user);

    int deleteById(int id);
}
