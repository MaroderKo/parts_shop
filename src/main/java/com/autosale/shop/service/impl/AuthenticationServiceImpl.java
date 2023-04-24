package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.AuthenticationService;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.UserService;
import com.autosale.shop.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final UserSessionService userSessionService;

    @Override
    public JwtTokensDTO loginByUserCredentials(User user) {
        user = userService.getVerifiedUser(user.getUserName(), user.getPassword());
        userSessionService.createSession(user);
        return jwtTokenService.generateTokensPair(user);
    }

    @Override
    public JwtTokensDTO loginByRefreshToken(String token) {
        JwtTokensDTO jwtTokensDTO = jwtTokenService.generateTokensFromRefreshToken(token);
        User userFromToken = jwtTokenService.parseUser(token);
        userSessionService.createSession(userFromToken);
        return jwtTokensDTO;
    }

}
