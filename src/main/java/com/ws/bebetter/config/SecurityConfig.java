package com.ws.bebetter.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) {
        httpSecurity
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(
                                        "/api/v1/auth/register/",
                                        "/api/v1/auth/login/",
                                        "/api/v1/auth/refresh-token/",
                                        "/api/v1/auth/password-recovery/",
                                        "/api/v1/auth/reset-password/"
                                )
                                .permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    @Profile("dev")
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(true);
    }

}
