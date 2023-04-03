package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDAO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {

    private final JwtTokenService jwtTokenService;

    @Override
    public JwtTokensDAO generateTokensByUserCredentials(User user) {
        return jwtTokenService.generateTokensPair(user);
    }

    @Override
    public JwtTokensDAO generateTokensByRefreshToken(String token) {
        return jwtTokenService.generateTokensFromRefreshToken(token);
    }

    @Override
    public boolean verifyToken(String access_token) {
        return jwtTokenService.isValidToken(access_token);
    }
}
