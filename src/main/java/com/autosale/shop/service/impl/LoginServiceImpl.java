package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final JwtTokenService jwtTokenService;

    @Override
    public JwtTokensDTO generateTokensByUserCredentials(User user) {
        return jwtTokenService.generateTokensPair(user);
    }

    @Override
    public JwtTokensDTO generateTokensByRefreshToken(String token) {
        return jwtTokenService.generateTokensFromRefreshToken(token);
    }

    @Override
    public boolean verifyToken(String access_token) {
        return jwtTokenService.isValidToken(access_token);
    }
}
