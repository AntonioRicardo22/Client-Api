package com.neoapp.cliente_api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customerApiOpenAPI() {
                Info info = new Info().title("Cliente API - NeoApp").description(
                                "REST API para cadastro e gestão de clientes pessoa física, incluindo endereço e filtros de busca.")
                                .version("v1")
                                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                                .contact(new Contact().name("NeoApp").email("contato@neoapp.example")
                                                .url("https://neoapp.example"));

                Server local = new Server().url("http://localhost:8080").description("Ambiente local");

                return new OpenAPI().info(info).servers(List.of(local)).components(new Components())
                                .externalDocs(new ExternalDocumentation().description("Documentação adicional")
                                                .url("https://neoapp.example/docs"));
        }
}
