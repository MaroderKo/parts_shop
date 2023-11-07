package com.autosale.shop.repository

import com.autosale.shop.model.User
import java.util.*

interface UserRepository {
    fun findAll(): List<User>
    fun findById(id: Int): Optional<User>
    fun findByUsername(userName: String): Optional<User>
    fun save(user: User): Optional<Int>
    fun update(user: User)
    fun deleteById(id: Int): Int
}
