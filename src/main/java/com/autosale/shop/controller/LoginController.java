package com.autosale.shop.controller;

import com.autosale.shop.model.JwtTokensDAO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokensDAO> login(@RequestBody User user)
    {
        return ResponseEntity.ok(securityService.generateTokensByUserCredentials(user));
    }
    @PostMapping("/tokens/refresh")
    public ResponseEntity<JwtTokensDAO> refreshTokens(@RequestBody Map<String, String> params)
    {
        return ResponseEntity.ok(securityService.generateTokensByRefreshToken(params.get("refresh_token")));
    }

    @PostMapping("/tokens/verify")
    @ResponseBody
    boolean verifyToken(@RequestBody JwtTokensDAO tokens)
    {
        return securityService.verifyToken(tokens.getAccessToken());
    }
}
