package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    JwtTokensDTO generateTokensByUserCredentials(User user);

    JwtTokensDTO generateTokensByRefreshToken(String token);

    boolean isAuthenticated(String token);

    Authentication getAuthentication(String token);
}
