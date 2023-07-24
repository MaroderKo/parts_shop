package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;

public interface AuthenticationService {

    //SOLID
    // S - Single responsibility
    // сервіс аутентифікації реалізує функціонал тільки аутентифікації

    JwtTokensDTO loginByUserCredentials(User user);

    JwtTokensDTO loginByRefreshToken(String token);

}
