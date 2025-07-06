package com.github.nelsonssoares.AuthGateway.services.impl;

import com.github.nelsonssoares.AuthGateway.domain.dto.UserRequest;
import com.github.nelsonssoares.AuthGateway.domain.repositories.UserRepository;
import com.github.nelsonssoares.AuthGateway.outlayers.gateways.UserGateway;
import com.github.nelsonssoares.AuthGateway.services.UserResgistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserResgistrationServiceImpl implements UserResgistrationService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserResgistrationServiceImpl.class);

    private final UserGateway userGateway;
    private final UserRepository repository;

    @Override
    public ResponseEntity<UserRequest> registerUser(UserRequest userRequest) throws Exception {

        UserRequest user = userGateway.createUser(userRequest);

        if (user == null || user.getId() == null || user.getId().describeConstable().isEmpty()) {
            throw new RuntimeException("User already exists or invalid data provided");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = repository.findByUsername(username);
        if (user != null) {
            return user;
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
