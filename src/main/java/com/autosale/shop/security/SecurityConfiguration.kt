package com.autosale.shop.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf()
            .disable()
            .sessionManagement()
            .disable()
            .authorizeHttpRequests { matcher ->
                matcher
                    .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/products/**").authenticated()
                    .requestMatchers("/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
                    .requestMatchers("/sessions/**").hasRole("ADMIN")
                    .requestMatchers("/backup/**").hasRole("ADMIN")
                    .requestMatchers("/error").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAt(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)
            .build()
    }
}
