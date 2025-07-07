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
import org.springframework.stereotype.Service;

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

}
