package com.autosale.shop.repository;

import org.jooq.Record1;

import java.util.List;
import java.util.Optional;

public interface BasicRepository<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    Optional<Record1<Integer>> save(T t);

    int update(T t);

    int deleteById(int id);
}
