package com.github.nelsonssoares.AuthGateway.domain.dto.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AccountCredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}
