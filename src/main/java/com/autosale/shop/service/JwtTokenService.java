package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;

public interface JwtTokenService {
    JwtTokensDTO generateTokensPair(User user);

    JwtTokensDTO generateTokensFromRefreshToken(String refreshToken);

    User parseUser(String token);
}
