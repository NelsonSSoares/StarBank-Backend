package com.github.nelsonssoares.AuthGateway.domain.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Permission implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String description;

    @Override
    public String getAuthority() {
        return this.description;
    }
}
