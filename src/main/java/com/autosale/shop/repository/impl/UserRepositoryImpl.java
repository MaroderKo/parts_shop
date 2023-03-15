package com.autosale.shop.repository.impl;


import com.autosale.shop.model.User;
import com.autosale.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static shop.domain.tables.Users.USERS;


@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    @Override
    public List<User> findAll() {
        return dsl.selectFrom(USERS)
                .fetchInto(User.class);
    }

    @Override
    public Optional<User> findById(int id) {
        return dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptionalInto(User.class);
    }

    @Override
    public Optional<Integer> save(User user) {
        return dsl.insertInto(USERS)
                .set(dsl.newRecord(USERS, user))
                .returningResult(USERS.ID)
                .fetchOptionalInto(Integer.class);
    }

    @Override
    public void update(User user) {
        dsl.newRecord(USERS,user)
                .update();

    }

    @Override
    public int deleteById(int id) {
        return dsl.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

}
