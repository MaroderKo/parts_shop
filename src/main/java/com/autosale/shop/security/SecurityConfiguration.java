package com.autosale.shop.security;

import com.autosale.shop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final UserService userService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http
                .csrf()
                .disable()
                .authorizeHttpRequests(matcher ->
                        matcher
                                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                                .requestMatchers("/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/verify").permitAll()
                                .requestMatchers(HttpMethod.POST,"/refresh").permitAll()
                                .anyRequest().authenticated())
                .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
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
