package com.autosale.shop.repository.impl

import com.autosale.shop.model.User
import com.autosale.shop.repository.UserRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import structure.tables.Users
import java.util.*


@Repository
class UserRepositoryImpl(private val dsl: DSLContext) : UserRepository {
    override fun findAll(): List<User> {
        return dsl.selectFrom(Users.USERS)
            .fetchInto(User::class.java)
    }

    override fun findById(id: Int): Optional<User> {
        return dsl.selectFrom(Users.USERS)
            .where(Users.USERS.ID.eq(id))
            .fetchOptionalInto(User::class.java)
    }

    override fun findByUsername(userName: String): Optional<User> {
        return dsl.selectFrom(Users.USERS)
            .where(Users.USERS.USER_NAME.eq(userName))
            .fetchOptionalInto(User::class.java)
    }

    override fun save(user: User): Optional<Int> {
        return dsl.insertInto(Users.USERS)
            .set(dsl.newRecord(Users.USERS, user))
            .returningResult(Users.USERS.ID)
            .fetchOptionalInto(Int::class.java)
    }

    override fun update(user: User) {
        dsl.newRecord(Users.USERS, user)
            .update()
    }

    override fun deleteById(id: Int): Int {
        return dsl.deleteFrom(Users.USERS)
            .where(Users.USERS.ID.eq(id))
            .execute()
    }
}
