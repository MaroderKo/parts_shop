package com.autosale.shop.service;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;

public interface JwtTokenService {

    //SOLID
    //I - Interface segregation principle
    //Інтерфейс декларує методи необхідні саме класу сервісу по роботі з токенами і не має зайвих методів

    JwtTokensDTO generateTokensPair(User user);

    JwtTokensDTO generateTokensFromRefreshToken(String refreshToken);

    User parseUser(String token);
}
