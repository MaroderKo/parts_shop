package com.autosale.shop.service.impl

import com.autosale.shop.model.User
import com.autosale.shop.model.UserSession
import com.autosale.shop.repository.UserSessionRepository
import com.autosale.shop.service.UserSessionService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserSessionServiceImpl(
    private val repository: UserSessionRepository
) : UserSessionService {

    override fun createSession(user: User): UserSession {
        val session = UserSession(UUID.randomUUID(), user)
        repository.create(user.id.toString(), session)
        return session
    }

    override fun terminateSession(id: Int): Boolean {
        return repository.delete(id.toString())!!
    }

    override fun getSession(user: User): UserSession {
        return repository.read(user.id.toString())
    }
}
