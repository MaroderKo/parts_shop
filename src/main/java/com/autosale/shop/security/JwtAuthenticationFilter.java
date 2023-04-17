package com.autosale.shop.security;

import com.autosale.shop.model.UserSession;
import com.autosale.shop.service.AuthenticationService;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final UserSessionService userSessionService;
    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> tokenOptional = extractToken(request);
        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            UserSession session = userSessionService.getSession(jwtTokenService.getUserFromToken(token));
            if (session != null) {
                Authentication authentication = authenticationService.getAuthentication(session.getUser());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(JWT_TOKEN_PREFIX)) {
            return Optional.of(header.replace(JWT_TOKEN_PREFIX, ""));
        }
        return Optional.empty();
    }
}
