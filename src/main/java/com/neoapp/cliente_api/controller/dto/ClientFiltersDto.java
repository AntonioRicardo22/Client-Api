package com.neoapp.cliente_api.controller.dto;

import com.neoapp.cliente_api.validator.StateValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Optional;

@Schema(name = "ClientFiltersDto", description = "Parâmetros opcionais de filtro para busca de clientes")
public record ClientFiltersDto(

                @Schema(description = "Nome (contém)", example = "Maria") @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s'-]+$", message = "{client.name.invalid}") String name,

                @Schema(description = "CPF (apenas números)", example = "12345678901") @Pattern(regexp = "^[0-9]+$", message = "{client.cpf.invalid}") @CPF String cpf,

                @Schema(description = "birthDate (data)", example = "1990-2-03") @Past(message = "{client.birthDate.invalid}") LocalDate birthDate,

                @Schema(description = "E-mail (contém)", example = "@exemplo.com") @Email String email,

                @Schema(description = "Telefone (contém)", example = "1198") @Pattern(regexp = "^[0-9]+$", message = "{client.phoneNumber.invalid}") String phoneNumber,

                @Schema(description = "Rua (contém)", example = "Paulista") @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s.,-]+$", message = "{client.street.invalid}") String street,

                @Schema(description = "Cidade (contém)", example = "São Paulo") @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s'.-]+$", message = "{client.city.invalid}") String city,

                @Schema(description = "CEP (apenas números)", example = "01310930") @Pattern(regexp = "^[0-9]+$", message = "{client.zipCode.invalid}") String zipCode,

                @Schema(description = "UF (2 letras)", example = "SP") @Size(min = 2, max = 2, message = "{client.state.invalid}") @StateValidator(message = "{client.state.invalid}") String state,
                @Schema(description = "Ordenação, ex: name,asc ou birthDate,desc", example = "name,asc", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Optional<String> sort) {
}
