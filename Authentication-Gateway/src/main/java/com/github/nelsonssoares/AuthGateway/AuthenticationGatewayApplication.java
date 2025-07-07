package com.github.nelsonssoares.AuthGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationGatewayApplication.class, args);
		generateHashedPassword();
	}

	private static void generateHashedPassword() {
		PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder
				("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

		Map<String, PasswordEncoder> enconders = new HashMap<>();
		enconders.put("pbkdf2", pbkdf2Enconder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);

		var pass = passwordEncoder.encode("admin123");
		System.out.println("Hashed password: " + pass );
	}

}

