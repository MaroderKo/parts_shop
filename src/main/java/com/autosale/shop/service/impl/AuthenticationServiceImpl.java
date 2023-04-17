package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import com.autosale.shop.service.AuthenticationService;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.UserService;
import com.autosale.shop.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final UserSessionService userSessionService;

    @Override
    public JwtTokensDTO generateTokensByUserCredentials(User user) {
        user = userService.getVerifiedUser(user.getUserName(), user.getPassword());
        userSessionService.createSession(user);
        return jwtTokenService.generateTokensPair(user);
    }

    @Override
    public JwtTokensDTO generateTokensByRefreshToken(String token) {
        JwtTokensDTO jwtTokensDTO = jwtTokenService.generateTokensFromRefreshToken(token);
        User userFromToken = jwtTokenService.getUserFromToken(token);
        userSessionService.createSession(userFromToken);
        return jwtTokensDTO;
    }

    @Override
    public boolean isAuthenticated(String token) {
        return userSessionService.getSession(jwtTokenService.getUserFromToken(token)) != null;
    }

    @Override
    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user.getId(), "", getAuthorities(user.getRole()));
    }

    @Override
    public void terminateSession(int id) {
        userSessionService.terminateSession(id);
    }

    private List<GrantedAuthority> getAuthorities(UserRole role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
