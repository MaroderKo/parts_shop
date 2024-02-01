package com.autosale.shop.service

import com.autosale.shop.model.JwtTokensDTO
import com.autosale.shop.model.User

interface JwtTokenService {
    fun generateTokensPair(user: User): JwtTokensDTO
    fun generateTokensFromRefreshToken(refreshToken: String): JwtTokensDTO
    fun parseUser(token: String): User
}
