package com.autosale.shop.controller;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<JwtTokensDTO> login(@RequestBody User user) {
        return ResponseEntity.ok(loginService.generateTokensByUserCredentials(user));
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<JwtTokensDTO> refreshTokens(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(loginService.generateTokensByRefreshToken(params.get("refresh_token")));
    }

    @PostMapping("/tokens/verify")
    @ResponseBody
    public boolean verifyToken(@RequestBody JwtTokensDTO tokens) {
        return loginService.isValidToken(tokens.getAccessToken());
    }
}
