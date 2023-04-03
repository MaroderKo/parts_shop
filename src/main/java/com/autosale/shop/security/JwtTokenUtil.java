package com.autosale.shop.security;

import com.autosale.shop.model.User;
import com.autosale.shop.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserService userService;


    public String generateAccessToken(User user) {
        return Jwts.builder().claim("id", user.getId().toString())
                .claim("username", user.getUserName())
                .claim("role", user.getRole())
                .setId(UUID.randomUUID().toString())
                .setSubject(String.format("%s,%s", user.getId(), user.getUserName()))
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).compact();

    }

    public String generateRefreshToken(String accessToken) throws JwtException {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(accessToken).getBody();
        return Jwts.builder()
                .claim("id", claims.getId())
                .claim("username", claims.get("username", String.class))
                .claim("role", claims.get("role", String.class))
                .claim("type", "refresh")
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).compact();
    }

    public boolean verifyToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();
        User user = userService.findById(Integer.parseInt(claims.get("id", String.class)));

        if (user == null) {
            return false;
        }

        return claims.get("username", String.class).equals(user.getUserName()) && claims.get("role", String.class).equals(user.getRole().name()) && claims.getExpiration().after(Date.from(Instant.now()));
    }

    public Authentication getAuthentication(String token)
    {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token).getBody();

        return new UsernamePasswordAuthenticationToken(claims.get("id", String.class),"",getAuthorities(claims));
    }

    private List<GrantedAuthority> getAuthorities(Claims claims)
    {
        return List.of(new SimpleGrantedAuthority("ROLE_"+claims.get("role", String.class)));
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(refreshToken).getBody();
        if (claims.get("type", String.class).isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The passed token is not a refresh token");
        return Jwts.builder()
                .claim("id", claims.getId())
                .claim("username", claims.get("username", String.class))
                .claim("role", claims.get("role", String.class))
                .setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).compact();
    }
}
