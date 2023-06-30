package com.autosale.shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    //Design pattern
    //Singletone - патерн проектування при якому об'єкт може буи створений лише 1 раз за весь час роботи додатку
    //В даному випадку його реалізує спрінг

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .sessionManagement()
                .disable()
                .authorizeHttpRequests(matcher ->
                        matcher
                                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/products/**").authenticated()
                                .requestMatchers("/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
                                .requestMatchers("/sessions/**").hasRole("ADMIN")
                                .requestMatchers("/backup/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .build();
    }
}
