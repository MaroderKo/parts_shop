package com.autosale.shop.service;

import com.autosale.shop.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(int id);

    Integer create(User user);

    void edit(User user);

    int delete(int id);
}
