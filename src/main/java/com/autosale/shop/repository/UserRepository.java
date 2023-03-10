package com.autosale.shop.repository;


import com.autosale.shop.model.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.domain.tables.records.UsersRecord;

import java.util.List;
import java.util.Optional;

import static shop.domain.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepository {

    private final DSLContext dsl;

    public List<User> findAll() {
        return dsl.selectFrom(USERS)
                .fetchInto(User.class);
    }

    public Optional<UsersRecord> findById(int id) {
        return dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional();
    }

    public Optional<UsersRecord> save(User user) {
        return dsl.insertInto(USERS)
                .set(dsl.newRecord(USERS, user))
                .returning(USERS.ID)
                .fetchOptional();
    }

    public int update(User user) {
        return dsl.update(USERS)
                .set(dsl.newRecord(USERS, user))
                .where(USERS.ID.eq(user.getId()))
                .execute();

    }

    public int deleteById(int id) {
        return dsl.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

}
