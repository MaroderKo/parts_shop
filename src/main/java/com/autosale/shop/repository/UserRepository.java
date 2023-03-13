package com.autosale.shop.repository;


import com.autosale.shop.model.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static shop.domain.tables.Users.USERS;


@RequiredArgsConstructor
@Component
public class UserRepository implements BasicRepository<User>{

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
                .fetchOptional()
                .map(usersRecord -> usersRecord.into(User.class));
    }

    @Override
    public Optional<Record1<Integer>> save(User user) {
        return dsl.insertInto(USERS)
                .set(dsl.newRecord(USERS, user))
                .returningResult(USERS.ID)
                .fetchOptional();
    }

    @Override
    public int update(User user) {
        return dsl.update(USERS)
                .set(dsl.newRecord(USERS, user))
                .where(USERS.ID.eq(user.getId()))
                .execute();

    }

    @Override
    public int deleteById(int id) {
        return dsl.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

}
