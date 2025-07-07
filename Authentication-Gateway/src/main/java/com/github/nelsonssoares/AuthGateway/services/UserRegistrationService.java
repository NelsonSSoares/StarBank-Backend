package com.github.nelsonssoares.AuthGateway.services;

import com.github.nelsonssoares.AuthGateway.domain.dto.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserRegistrationService {

    ResponseEntity<UserRequest> registerUser(UserRequest userRequest) throws Exception;

}
