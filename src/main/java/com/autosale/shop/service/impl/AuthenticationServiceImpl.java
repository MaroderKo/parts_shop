package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.AuthenticationService;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Override
    public JwtTokensDTO generateTokensByUserCredentials(User user) {

        user = userService.getVerifiedUser(user.getUserName(), user.getPassword());
        return jwtTokenService.generateTokensPair(user);
    }

    @Override
    public JwtTokensDTO generateTokensByRefreshToken(String token) {
        return jwtTokenService.generateTokensFromRefreshToken(token);
    }

    @Override
    public boolean isAuthenticated(String token) {
        Claims claims = jwtTokenService.getClaimsFromToken(token);
        User user = userService.findById(Integer.parseInt(claims.get("id", String.class)));
        if (user == null) {
            return false;
        }
        return claims.get("username", String.class).equals(user.getUserName()) && claims.get("role", String.class).equals(user.getRole().name()) && claims.getExpiration().after(new Date());
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = jwtTokenService.getClaimsFromToken(token);
        return new UsernamePasswordAuthenticationToken(claims.get("id", String.class), "", getAuthorities(claims));

    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class)));
    }
}
