package com.dd25.dietiestates25.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests
            (
                auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/accounts/forgot-password").permitAll()
                .requestMatchers("/api/accounts/**").authenticated()

                .requestMatchers("/api/listings/search").permitAll()
                .requestMatchers("/api/listings/active").permitAll()
                .requestMatchers("/api/listings/view/**").permitAll()
                .requestMatchers("/api/listings/**").hasRole("COMPANY")

                .requestMatchers("api/location/**").permitAll()

                .requestMatchers("/api/company/**").hasRole("COMPANY")

                .anyRequest().authenticated()
            )
            .sessionManagement
            (
                session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}