package com.autosale.shop.controller;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JwtTokensDTO> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.loginByUserCredentials(user));
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<JwtTokensDTO> refreshTokens(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(authenticationService.loginByRefreshToken(params.get("refreshToken")));
    }

}
