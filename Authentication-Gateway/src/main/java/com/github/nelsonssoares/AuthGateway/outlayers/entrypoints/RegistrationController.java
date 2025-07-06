package com.github.nelsonssoares.AuthGateway.outlayers.entrypoints;

import com.github.nelsonssoares.AuthGateway.domain.dto.UserRequest;
import com.github.nelsonssoares.AuthGateway.outlayers.entrypoints.docs.RegistrationControllerDoc;
import com.github.nelsonssoares.AuthGateway.services.UserResgistrationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    private final UserResgistrationService service;

    @PostMapping(value = REGISTER_USER)
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<UserRequest> createUser(UserRequest dto) throws Exception {
        UserRequest user = service.registerUser(dto).getBody();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }
}
