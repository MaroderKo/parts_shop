package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDAO;
import com.autosale.shop.model.User;

public interface SecurityService {

    JwtTokensDAO generateTokensByUserCredentials(User user);

    JwtTokensDAO generateTokensByRefreshToken(String token);

    boolean verifyToken(String access_token);
}
