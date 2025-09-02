package com.neoapp.cliente_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;

@Schema(name = "ClienteFiltersDto", description = "Parâmetros opcionais de filtro para busca de clientes")
public record ClienteFiltersDto(
        @Schema(description = "Nome (contém)", example = "Maria") String name,
        @Schema(description = "CPF (apenas números)", example = "12345678901") String cpf,
        @Schema(description = "E-mail (contém)", example = "@exemplo.com") String email,
        @Schema(description = "Telefone (contém)", example = "1198") String phoneNumber,
        @Schema(description = "Rua (contém)", example = "Paulista") String street,
        @Schema(description = "Cidade (contém)", example = "São Paulo") String city,
        @Schema(description = "CEP (apenas números)", example = "01310930") String zipCode,
        @Schema(description = "UF (2 letras)", example = "SP") String state,
        @Schema(description = "Ordenação, ex: name,asc ou birthDate,desc", example = "name,asc", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Optional<String> sort
) {
}
