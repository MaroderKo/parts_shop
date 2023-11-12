package com.autosale.shop.service.impl

import com.autosale.shop.exception.InvalidOperationException
import com.autosale.shop.model.JwtTokensDTO
import com.autosale.shop.model.User
import com.autosale.shop.model.UserRole
import com.autosale.shop.service.JwtTokenService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtTokenServiceImpl(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.access-token-lifetime}") private val accessTokenTTLInMinutes: Int,
    @Value("\${jwt.refresh-token-lifetime}") private val refreshTokenTTLInHours: Int
) : JwtTokenService {
    override fun generateTokensPair(user: User): JwtTokensDTO {
        val (id, userName, _, role) = user
        val jwtBuilder = Jwts.builder().claim("id", id.toString())
            .claim("username", userName)
            .claim("role", role)
            .setId(UUID.randomUUID().toString())
            .setSubject(String.format("$id,$userName"))
            .setIssuedAt(Date()).setExpiration(
                Date.from(
                    Instant.now().plus(
                        accessTokenTTLInMinutes.toLong(), ChronoUnit.MINUTES
                    )
                )
            )
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
        val accessToken = jwtBuilder.compact()
        val refreshToken = jwtBuilder
            .claim("type", "refresh")
            .setIssuedAt(Date()).setExpiration(
                Date.from(
                    Instant.now().plus(
                        refreshTokenTTLInHours.toLong(), ChronoUnit.HOURS
                    )
                )
            )
            .compact()
        return JwtTokensDTO(accessToken, refreshToken)
    }

    @Throws(UnsupportedJwtException::class)
    override fun generateTokensFromRefreshToken(refreshToken: String): JwtTokensDTO {
        val claims = getClaimsFromToken(refreshToken)
        if ("refresh" != claims.get("type", String::class.java)) {
            throw InvalidOperationException("Jwt token is not a refresh token")
        }
        return generateTokensPair(
            User(
                claims.get("id", String::class.java).toInt(),
                claims.get("username", String::class.java),
                "",
                UserRole.valueOf(claims.get("role", String::class.java))
            )
        )
    }

    override fun parseUser(token: String): User {
        return getUserFromClaims(getClaimsFromToken(token))
    }

    private fun getClaimsFromToken(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getUserFromClaims(claims: Claims): User {
        return User(
            claims.get("id", String::class.java).toInt(),
            claims.get("username", String::class.java),
            "",
            UserRole.valueOf(claims.get("role", String::class.java))
        )
    }
}
