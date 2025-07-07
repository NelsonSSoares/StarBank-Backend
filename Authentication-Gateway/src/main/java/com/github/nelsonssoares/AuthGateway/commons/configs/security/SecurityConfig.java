package com.github.nelsonssoares.AuthGateway.commons.configs.security;

import com.github.nelsonssoares.AuthGateway.security.jwt.JwtTokenFilter;
import com.github.nelsonssoares.AuthGateway.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {

        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder
                ("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> enconders = new HashMap<>();
        enconders.put("pbkdf2", pbkdf2Enconder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);

        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider);
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests
                                        // Libera os endpoints de autenticação
                                        .requestMatchers(
                                                "/starbank/auth/signup",
                                                "/starbank/auth/signin",
                                                "/starbank/auth/refresh/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**"
                                        ).permitAll()

                                        // Libera apenas POST para /starbank/users
                                        .requestMatchers(HttpMethod.POST, "/starbank/users").permitAll()

                                        // Bloqueia qualquer outro acesso a /users/**
                                        .requestMatchers("/starbank/users/**").authenticated()

                                        // Qualquer outro endpoint precisa de autenticação
                                        .anyRequest().authenticated()
                )
                .cors(cors -> {}) // ou .disable()
                .build();
    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        JwtTokenFilter filter = new JwtTokenFilter(tokenProvider);
//        return http
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(
//                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(
//                        authorizeHttpRequests ->
//                                authorizeHttpRequests
//                                        .requestMatchers(
//                                                "/starbank/auth/signup",
//                                                "/starbank/auth/signin",
//                                                "/starbank/auth/refresh/**",
//                                                "/starbank/users",
//                                                "/swagger-ui/**",
//                                                "/v3/api-docs/**"
//                                        ).permitAll()
//                                        .requestMatchers(
//                                                "/users/**"
//                                        ).authenticated()
//
//                )
//                .cors(cors -> {}/*cors.disable()*/)
//                .build();
//
//    }
}
