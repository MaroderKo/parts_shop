package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import org.springframework.security.core.Authentication;

public interface JwtTokenService {
    JwtTokensDTO generateTokensPair(User user);

    boolean isValidToken(String token);

    Authentication getAuthentication(String token);

    JwtTokensDTO generateTokensFromRefreshToken(String refreshToken);
}
