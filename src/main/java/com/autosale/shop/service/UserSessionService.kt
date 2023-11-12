package com.autosale.shop.service

import com.autosale.shop.model.User
import com.autosale.shop.model.UserSession

interface UserSessionService {
    fun createSession(user: User): UserSession
    fun terminateSession(id: Int): Boolean
    fun getSession(user: User): UserSession
}
