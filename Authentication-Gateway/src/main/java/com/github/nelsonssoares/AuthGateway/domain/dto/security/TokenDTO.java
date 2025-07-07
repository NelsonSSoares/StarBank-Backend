package com.github.nelsonssoares.AuthGateway.domain.dto.security;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;


}
