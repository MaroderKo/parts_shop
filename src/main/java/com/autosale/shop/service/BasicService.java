package com.autosale.shop.service;

import com.autosale.shop.model.User;

import java.util.List;

public interface BasicService<T> {
    List<T> findAll();

    User findById(int id);

    Integer create(T t);

    int edit(T t);

    int delete(int id);
}
