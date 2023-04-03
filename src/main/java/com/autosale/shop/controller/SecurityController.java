package com.autosale.shop.controller;

import com.autosale.shop.model.User;
import com.autosale.shop.security.JwtTokenUtil;
import com.autosale.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    ResponseEntity<Map<String, String>> login(@RequestBody User user)
    {
        Map<String, String> answer = new HashMap<>();
        String accessToken = jwtTokenUtil.generateAccessToken(userService.returnVerifiedUser(user.getUserName(), user.getPassword()));
        answer.put("access_token",accessToken);
        answer.put("refresh_token",jwtTokenUtil.generateRefreshToken(accessToken));
        return ResponseEntity.ok(answer);


    }
    @PostMapping("/refresh")
    ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> params)
    {
        String refreshToken = params.get("refresh_token");
        Map<String, String> answer = new HashMap<>();
        String accessToken = jwtTokenUtil.generateAccessTokenFromRefreshToken(refreshToken);
        answer.put("access_token",accessToken);
        answer.put("refresh_token",jwtTokenUtil.generateRefreshToken(accessToken));
        return ResponseEntity.ok(answer);


    }

    @PostMapping("/verify")
    @ResponseBody
    boolean verifyToken(@RequestBody Map<String, String> data)
    {
        return jwtTokenUtil.verifyToken(data.get("access_token"));
    }
}
