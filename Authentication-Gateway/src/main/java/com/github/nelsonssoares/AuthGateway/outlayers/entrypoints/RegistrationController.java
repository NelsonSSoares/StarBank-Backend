package com.github.nelsonssoares.AuthGateway.outlayers.entrypoints;

import com.github.nelsonssoares.AuthGateway.domain.dto.UserRequest;
import com.github.nelsonssoares.AuthGateway.domain.dto.security.AccountCredentialsDTO;
import com.github.nelsonssoares.AuthGateway.domain.dto.security.TokenDTO;
import com.github.nelsonssoares.AuthGateway.outlayers.entrypoints.docs.RegistrationControllerDoc;
import com.github.nelsonssoares.AuthGateway.services.UserRegistrationService;
import com.github.nelsonssoares.AuthGateway.services.impl.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.nelsonssoares.AuthGateway.commons.constants.ControllerConstants.*;
import static org.springframework.http.MediaType.*;


@CrossOrigin(value = "${cors.originsPatterns}")
@Tag(name = API_TAG, description = API_DESCRIPTION)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = API_BASE_URL, produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE, APPLICATION_YAML_VALUE})
public class RegistrationController implements RegistrationControllerDoc {

    private final UserRegistrationService service;
    private final AuthService authService;

    @PostMapping(value = REGISTER_USER)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<UserRequest> createUser(@RequestBody @Valid UserRequest dto) throws Exception {
        UserRequest user = service.registerUser(dto).getBody();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = LOGIN_USER)
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials) throws Exception {

        if(credentialsIsInvalid(credentials)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid client request, username and password must be provided");
        }

        var token = authService.signin(credentials);
        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid client request, username or password is incorrect");
        }

        return ResponseEntity.ok().body(token);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return  (credentials.getUsername() == null || credentials.getPassword() == null || StringUtils.isBlank(credentials.getUsername())
        || StringUtils.isBlank(credentials.getPassword()));

    }


}
