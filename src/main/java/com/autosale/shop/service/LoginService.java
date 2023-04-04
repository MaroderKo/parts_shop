package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;

public interface LoginService {

    JwtTokensDTO generateTokensByUserCredentials(User user);

    JwtTokensDTO generateTokensByRefreshToken(String token);

    boolean verifyToken(String access_token);
}
