package com.autosale.shop.security

import com.autosale.shop.service.JwtTokenService
import com.autosale.shop.service.UserSessionService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component

class JwtAuthenticationFilter(
    private val userSessionService: UserSessionService,
    private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        extractToken(request)?.let { token ->
            userSessionService.getSession(jwtTokenService.parseUser(token)).let { session ->
                val (id, _, _, role) = session.user
                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    id, "", listOf(SimpleGrantedAuthority("ROLE_" + role!!.name))
                )
                SecurityContextHolder.getContext().authentication = authentication

            }
        }
        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        return header?.takeIf { header.startsWith(JWT_TOKEN_PREFIX) }?.replace(JWT_TOKEN_PREFIX, "")
    }

    companion object {
        private const val JWT_TOKEN_PREFIX = "Bearer "
    }
}
