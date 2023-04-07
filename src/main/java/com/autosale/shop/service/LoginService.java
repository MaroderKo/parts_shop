package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import org.springframework.security.core.Authentication;

public interface LoginService {

    JwtTokensDTO generateTokensByUserCredentials(User user);

    JwtTokensDTO generateTokensByRefreshToken(String token);

    boolean isValidToken(String access_token);

    Authentication getAuthentication(String token);
}
