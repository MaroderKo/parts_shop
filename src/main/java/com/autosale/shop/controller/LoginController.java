package com.autosale.shop.controller;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JwtTokensDTO> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.generateTokensByUserCredentials(user));
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<JwtTokensDTO> refreshTokens(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(authenticationService.generateTokensByRefreshToken(params.get("refresh_token")));
    }

    @PostMapping("/tokens/verify")
    @ResponseBody
    public boolean verifyToken(@RequestBody JwtTokensDTO tokens) {
        return authenticationService.isAuthenticated(tokens.getAccessToken());
    }

    @PostMapping("/tokens/terminate/{id}")
    @ResponseBody
    public void terminateSession(@PathVariable("id") int id) {
        authenticationService.terminateSession(id);
    }
}
