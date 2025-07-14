package com.github.nelsonssoares.userapi.commons.configs.email;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class EmailConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String from;
    private boolean ssl;
}
