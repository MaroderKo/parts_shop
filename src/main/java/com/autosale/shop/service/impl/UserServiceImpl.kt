package com.autosale.shop.service.impl

import com.autosale.shop.exception.AuthenticationException
import com.autosale.shop.model.User
import com.autosale.shop.repository.UserRepository
import com.autosale.shop.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.springframework.security.core.userdetails.User as UserDetailsBuilder

@Service

class UserServiceImpl(
    private val repository: UserRepository,
    private val encoder: PasswordEncoder
) : UserService {

    override fun findAll(): List<User> {
        return repository.findAll()
    }

    override fun findById(id: Int): User {
        return repository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find user with id $id") }
    }

    override fun create(user: User): Int {
        return repository.save(copyWithPasswordEncoded(user))
            .orElseThrow { ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save user to database") }
    }

    override fun edit(user: User) {
        repository.update(copyWithPasswordEncoded(user))
    }

    override fun delete(id: Int): Int {
        return repository.deleteById(id)
    }

    override fun findByUsername(username: String): User {
        return repository.findByUsername(username).orElseThrow { UsernameNotFoundException("Bad credentials") }
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val (_, userName, password, role) = repository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found") }

        return UserDetailsBuilder.builder()
            .username(userName)
            .password(password)
            .roles(role!!.name)
            .build()
    }

    override fun getVerifiedUser(username: String, password: String): User {
        val user = repository.findByUsername(username)
        if (user.isPresent && encoder.matches(password, user.get().password)) {
            return user.get()
        }
        throw AuthenticationException("Bad credentials")
    }

    private fun copyWithPasswordEncoded(user: User): User {
        return User(user.id, user.userName, encoder.encode(user.password), user.role)
    }
}
