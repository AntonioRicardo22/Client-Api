package com.neoapp.cliente_api.controller.dto;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClientResponseDto", description = "Resposta com dados do cliente e endereço")
public record ClientResponseDto(

        @Schema(description = "Nome completo do cliente", example = "Maria Silva")
        String name,
        @Schema(description = "CPF do cliente", example = "12345678901")
        String cpf,
        @Schema(description = "Data de nascimento", example = "1990-05-22")
        LocalDate birthDate,
        @Schema(description = "E-mail do cliente", example = "maria.silva@exemplo.com")
        String email,
        @Schema(description = "Telefone do cliente", example = "11987654321")
        String phoneNumber,
        @Schema(description = "Logradouro", example = "Av. Paulista, 1000")
        String street,
        @Schema(description = "Cidade", example = "São Paulo")
        String city,
        @Schema(description = "CEP", example = "01310930")
        String zipCode,
        @Schema(description = "UF", example = "SP")
        String state,
        @Schema(description = "Idade calculada em anos", example = "34")
        Integer years

)
{ }
