package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import io.jsonwebtoken.Claims;

public interface JwtTokenService {
    JwtTokensDTO generateTokensPair(User user);

    JwtTokensDTO generateTokensFromRefreshToken(String refreshToken);

    Claims getClaimsFromToken(String token);
}
