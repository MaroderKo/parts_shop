package com.autosale.shop.service;

import com.autosale.shop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();

    User findById(int id);

    Integer create(User user);

    void edit(User user);

    int delete(int id);

    User findByUsername(String username);

    User returnVerifiedUser(String username, String password);
}
