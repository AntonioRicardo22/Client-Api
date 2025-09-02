package com.neoapp.cliente_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Enable auditing
public class ClienteApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteApiApplication.class, args);
	}

}
