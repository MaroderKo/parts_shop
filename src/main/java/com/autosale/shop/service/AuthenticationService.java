package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;

public interface AuthenticationService {

    JwtTokensDTO loginByUserCredentials(User user);

    JwtTokensDTO loginByRefreshToken(String token);

}
