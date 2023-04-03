package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDAO;
import com.autosale.shop.model.User;
import org.springframework.security.core.Authentication;

public interface JwtTokenService {
    JwtTokensDAO generateTokensPair(User user);

    boolean isValidToken(String token);

    Authentication getAuthentication(String token);

    JwtTokensDAO generateTokensFromRefreshToken(String refreshToken);
}
