package com.autosale.shop.service

import com.autosale.shop.model.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun findAll(): List<User>
    fun findById(id: Int): User
    fun create(user: User): Int
    fun edit(user: User)
    fun delete(id: Int): Int
    fun findByUsername(username: String): User
    fun getVerifiedUser(username: String, password: String): User
}
