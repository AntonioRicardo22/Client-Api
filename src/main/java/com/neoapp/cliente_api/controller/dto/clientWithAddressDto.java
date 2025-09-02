package com.neoapp.cliente_api.controller.dto;

import com.neoapp.cliente_api.model.Address;
import com.neoapp.cliente_api.model.Client;
import com.neoapp.cliente_api.validator.StateValidator;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "clientWithAddressDto", description = "Dados para criação/atualização de cliente com endereço")
public record clientWithAddressDto(

                @NotBlank(message = "{client.name.empty}") @Schema(description = "Nome completo do cliente", example = "Maria Silva") @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s'-]+$", message = "{client.name.invalid}") String name,
                @NotBlank(message = "{client.cpf.empty}") @Pattern(regexp = "^[0-9]+$", message = "{client.cpf.invalid}") @CPF @Schema(description = "CPF do cliente (apenas números)", example = "12345678901") String cpf,
                @NotNull(message = "{client.bitrhDate.empty}") @Past(message = "{client.birthDate.invalid}") @Schema(description = "Data de nascimento (yyyy-MM-dd)", example = "1990-05-22") LocalDate birthDate,
                @Email @NotBlank(message = "{client.email.empty}") @Schema(description = "E-mail válido do cliente", example = "maria.silva@exemplo.com") String email,
                @NotBlank(message = "{client.phoneNumber.empty}") @Pattern(regexp = "^[0-9]+$", message = "{client.phoneNumber.invalid}") @Schema(description = "Telefone do cliente (apenas números)", example = "11987654321") String phoneNumber,
                @NotBlank(message = "{client.street.empty}") @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s.,-]+$", message = "{client.street.invalid}") @Schema(description = "Logradouro", example = "Av. Paulista, 1000") String street,
                @NotBlank(message = "{client.city.empty}") @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚâêîôûÂÊÎÔÛãõÃÕüÜçÇ\\s'.-]+$", message = "{client.city.invalid}") @Schema(description = "Cidade", example = "São Paulo") String city,
                @NotBlank(message = "{client.zipCode.empty}") @Pattern(regexp = "^[0-9]+$", message = "{client.zipCode.invalid}") @Schema(description = "CEP (apenas números)", example = "01310930") String zipCode,
                @NotBlank(message = "{client.state.empty}") @Size(min = 2, max = 2, message = "{client.state.invalid}") @StateValidator(message = "{client.state.invalid}") @Schema(description = "UF (2 letras)", example = "SP") String state) {

        public Client mappedToClient() {
                Client client = new Client();
                client.setName(this.name);
                client.setCpf(this.cpf);
                client.setBirthDate(this.birthDate);
                client.setEmail(this.email);
                client.setPhoneNumber(this.phoneNumber);

                return client;
        }

        public Address mappedToAddress() {
                Address address = new Address();
                address.setStreet(this.street);
                address.setCity(this.city);
                address.setZipCode(this.zipCode);
                address.setState(this.state);

                return address;
        }

}
