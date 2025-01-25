package com.moneyfli.transaction_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.moneyfli.transaction_service.filter.JWTFilter;


@Configuration
public class SecurityConfig {

    @Autowired
    private JWTFilter JWTFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .anyRequest().authenticated())
                .addFilterBefore(JWTFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(customizer -> customizer.disable())
                .httpBasic(customizer -> Customizer.withDefaults())
                .build();
    }
}
