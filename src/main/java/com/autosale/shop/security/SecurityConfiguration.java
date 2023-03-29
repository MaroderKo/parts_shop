package com.autosale.shop.security;

import com.autosale.shop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http
                .csrf()
                .disable()
                .authorizeHttpRequests(matcher ->
                        matcher
                                .requestMatchers(HttpMethod.GET, "/users").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                                .requestMatchers("/users*").hasRole("ADMIN")
                                .requestMatchers("/users/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .httpBasic()
                .and()
                .authenticationManager(authManager)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .and()
                .inMemoryAuthentication()
                .withUser("user")
                .password("$2a$12$Z5x7TIuGUBwt4sNuzRFZEuN/U3KZyAFoHFrV0SoXO4/UVHUMRpvOS")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("$2a$12$Fv28Z52dz4jOxByPKCBdOOXar1Np9DXlfyz0sv0HUo0z5AYkIQ/k.")
                .roles("ADMIN")
                .and()
                .and()
                .build();
    }


}
