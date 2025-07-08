package com.github.nelsonssoares.AuthGateway.services.impl;

import com.github.nelsonssoares.AuthGateway.domain.dto.security.AccountCredentialsDTO;
import com.github.nelsonssoares.AuthGateway.domain.dto.security.TokenDTO;
import com.github.nelsonssoares.AuthGateway.domain.repositories.UserRepository;
import com.github.nelsonssoares.AuthGateway.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository repository;

    public ResponseEntity<TokenDTO> signin(AccountCredentialsDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );
        var user = repository.findByUsername(credentials.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("Username not found: " + credentials.getUsername());
        }
        var token = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken){
        TokenDTO token ;
        var user = repository.findByUsername(username);
        if(user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        }else{
            throw new UsernameNotFoundException("Username not found: " + username);
        }

        return ResponseEntity.ok().body(token);

    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder
                ("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> enconders = new HashMap<>();
        enconders.put("pbkdf2", pbkdf2Enconder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);

        return passwordEncoder.encode(password);

    }

}
