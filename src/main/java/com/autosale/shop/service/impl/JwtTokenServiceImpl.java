package com.autosale.shop.service.impl;

import com.autosale.shop.model.JwtTokensDAO;
import com.autosale.shop.model.User;
import com.autosale.shop.model.UserRole;
import com.autosale.shop.service.JwtTokenService;
import com.autosale.shop.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final String SECRET_KEY;

    private final UserService userService;

    private final int ACCESS_TOKEN_LIFETIME;

    private final int REFRESH_TOKEN_LIFETIME;

    public JwtTokenServiceImpl(@Value("${jwt.secret}") String secret_key, UserService userService, @Value("${jwt.access_token_lifetime}") int access_token_lifetime, @Value("${jwt.refresh_token_lifetime}") int refresh_token_lifetime) {
        SECRET_KEY = secret_key;
        this.userService = userService;
        ACCESS_TOKEN_LIFETIME = access_token_lifetime;
        REFRESH_TOKEN_LIFETIME = refresh_token_lifetime;
    }

    @Override
    public JwtTokensDAO generateTokensPair(User user) {
        if (user.getId() == null) {
            user = userService.getVerifiedUser(user.getUserName(), user.getPassword());
        }
        JwtBuilder jwtBuilder = Jwts.builder().claim("id", user.getId().toString())
                .claim("username", user.getUserName())
                .claim("role", user.getRole())
                .setId(UUID.randomUUID().toString())
                .setSubject(String.format("%s,%s", user.getId(), user.getUserName()))
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(ACCESS_TOKEN_LIFETIME, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()));
        String accessToken = jwtBuilder.compact();
        String refreshToken = jwtBuilder
                .claim("type", "refresh")
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(REFRESH_TOKEN_LIFETIME, ChronoUnit.HOURS)))
                .compact();
        return new JwtTokensDAO(accessToken,refreshToken);
    }


    @Override
    public boolean isValidToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
        User user = userService.findById(Integer.parseInt(claims.get("id", String.class)));

        if (user == null) {
            return false;
        }

        return claims.get("username", String.class).equals(user.getUserName()) && claims.get("role", String.class).equals(user.getRole().name()) && claims.getExpiration().after(new Date());
    }

    @Override
    public Authentication getAuthentication(String token)
    {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();

        return new UsernamePasswordAuthenticationToken(claims.get("id", String.class),"",getAuthorities(claims));
    }

    @Override
    public JwtTokensDAO generateTokensFromRefreshToken(String refreshToken) throws UnsupportedJwtException {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(refreshToken).getBody();
        if (!claims.get("type", String.class).equals("refresh")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt token is not a refresh token");
        }
        return generateTokensPair(new User(
                Integer.parseInt(claims.get("id", String.class)),
                claims.get("username", String.class),
                null,
                UserRole.valueOf(claims.get("role", String.class))));
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class)));
    }
}
