package com.autosale.shop.repository

import com.autosale.shop.model.UserSession

interface UserSessionRepository {
    fun create(key: String, session: UserSession)
    fun read(key: String): UserSession
    fun delete(key: String): Boolean?
}
