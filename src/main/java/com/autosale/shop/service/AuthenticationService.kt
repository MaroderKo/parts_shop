package com.autosale.shop.service

import com.autosale.shop.model.JwtTokensDTO
import com.autosale.shop.model.User

interface AuthenticationService {
    fun loginByUserCredentials(user: User): JwtTokensDTO
    fun loginByRefreshToken(token: String): JwtTokensDTO
}
