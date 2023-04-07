package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDTO;
import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import com.autosale.shop.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final String secretKey;
    private final int accessTokenTTLInMinutes;
    private final int refreshTokenTTLInHours;

    public JwtTokenServiceImpl(@Value("${jwt.secret}") String secretKey, @Value("${jwt.access-token-lifetime}") int accessTokenTTLInMinutes, @Value("${jwt.refresh-token-lifetime}") int refreshTokenTTLInHours) {
        this.secretKey = secretKey;
        this.accessTokenTTLInMinutes = accessTokenTTLInMinutes;
        this.refreshTokenTTLInHours = refreshTokenTTLInHours;
    }

    @Override
    public JwtTokensDTO generateTokensPair(User user) {
        JwtBuilder jwtBuilder = Jwts.builder().claim("id", user.getId().toString())
                .claim("username", user.getUserName())
                .claim("role", user.getRole())
                .setId(UUID.randomUUID().toString())
                .setSubject(String.format("%s,%s", user.getId(), user.getUserName()))
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(accessTokenTTLInMinutes, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()));
        String accessToken = jwtBuilder.compact();
        String refreshToken = jwtBuilder
                .claim("type", "refresh")
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(refreshTokenTTLInHours, ChronoUnit.HOURS)))
                .compact();
        return new JwtTokensDTO(accessToken, refreshToken);
    }

    @Override
    public JwtTokensDTO generateTokensFromRefreshToken(String refreshToken) throws UnsupportedJwtException {
        Claims claims = getClaimsFromToken(refreshToken);
        if (!"refresh".equals(claims.get("type", String.class))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt token is not a refresh token");
        }
        return generateTokensPair(new User(
                Integer.parseInt(claims.get("id", String.class)),
                claims.get("username", String.class),
                null,
                UserRole.valueOf(claims.get("role", String.class))));
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
