package com.autosale.shop.service.impl

import com.autosale.shop.logger.Logger
import com.autosale.shop.model.JwtTokensDTO
import com.autosale.shop.model.User
import com.autosale.shop.service.AuthenticationService
import com.autosale.shop.service.JwtTokenService
import com.autosale.shop.service.UserService
import com.autosale.shop.service.UserSessionService
import com.autosale.shop.util.MetricUtil
import io.micrometer.core.annotation.Counted
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    private val jwtTokenService: JwtTokenService,
    private val userService: UserService,
    private val userSessionService: UserSessionService,
) : AuthenticationService {


    @Counted(value = MetricUtil.Counter.USERS_LOGIN)
    override fun loginByUserCredentials(user: User): JwtTokensDTO {
        val (_, userName, password, _) = user
        val fullUser = userService.getVerifiedUser(userName, password)
        return generateSessionTokens(fullUser, userName)
    }

    private fun generateSessionTokens(fullUser: User, userName: String): JwtTokensDTO {
        userSessionService.createSession(fullUser)
        Logger.Info("User $userName logged in!").log()
        return jwtTokenService.generateTokensPair(fullUser)
    }

    override fun loginByRefreshToken(token: String): JwtTokensDTO {
        val jwtTokensDTO = jwtTokenService.generateTokensFromRefreshToken(token)
        val userFromToken = jwtTokenService.parseUser(token)
        userSessionService.createSession(userFromToken)
        return jwtTokensDTO
    }
}
