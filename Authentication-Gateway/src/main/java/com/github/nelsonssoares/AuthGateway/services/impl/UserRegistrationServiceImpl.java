package com.github.nelsonssoares.AuthGateway.services.impl;

import com.github.nelsonssoares.AuthGateway.domain.dto.UserRequest;
import com.github.nelsonssoares.AuthGateway.domain.entities.User;
import com.github.nelsonssoares.AuthGateway.domain.repositories.UserRepository;
import com.github.nelsonssoares.AuthGateway.outlayers.gateways.UserGateway;
import com.github.nelsonssoares.AuthGateway.services.UserRegistrationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService, UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private final UserGateway userGateway;
    private final UserRepository repository;


    @Transactional
    @Override
    public ResponseEntity<UserRequest> registerUser(UserRequest newUser) throws Exception {

        UserRequest userRequest = userGateway.createUser(newUser);

        if (userRequest == null || userRequest.getId() == null ) {
            throw new RuntimeException("User already exists or invalid data provided");
        }

        User user = new User();
        user.setUsername(userRequest.getEmail());
        user.setPassword(generateHashedPassword(userRequest.getPassword()));
        user.setFullName(userRequest.getName() + " " + userRequest.getLastName());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRequest);
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
