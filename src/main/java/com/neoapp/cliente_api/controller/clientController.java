package com.neoapp.cliente_api.controller;
import com.neoapp.cliente_api.controller.dto.ClienteFiltersDto;
import com.neoapp.cliente_api.controller.dto.clientResponseDto;
import com.neoapp.cliente_api.controller.dto.clientWithAddressDto;
import com.neoapp.cliente_api.model.Client;
import com.neoapp.cliente_api.service.clientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/client")
@Validated
@Tag(name = "Clientes", description = "Operações para gerenciar clientes e seus endereços")
public class clientController {

    private final clientService clientService;

    @PostMapping
    @Operation(summary = "Criar cliente",
            description = "Cria um cliente e endereço associado. Retorna o recurso criado com cabeçalho Location.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = clientResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
                    @ApiResponse(responseCode = "409", description = "CPF já cadastrado", content = @Content)
            })
    public ResponseEntity<clientResponseDto> save(
            @RequestBody(description = "Dados do cliente com endereço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = clientWithAddressDto.class)))
            @org.springframework.web.bind.annotation.RequestBody @Valid clientWithAddressDto dto){
       Client client = clientService.saveClientWithAddress(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(client.getId()).toUri();

       clientResponseDto toDto = clientService.mapToResponseDto(client);
        return ResponseEntity.created(uri).body(toDto);
    }

    @GetMapping ("/{cpf}")
    @Operation(summary = "Buscar por CPF", description = "Recupera um cliente pelo CPF",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = clientResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
            })
    public ResponseEntity<clientResponseDto>findByCpf( @PathVariable ("cpf")
                                                           @Pattern(regexp = "^[0-9]{11}$", message = "CPF deve conter apenas 11 dígitos numéricos")
    @Parameter(description = "CPF do cliente (apenas números)", example = "12345678901") String cpf){
       clientResponseDto client = clientService.findByCpf(cpf);
       return ResponseEntity.ok(client);
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar por CPF", description = "Atualiza dados de um cliente existente pelo CPF",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = clientResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
            })
    public ResponseEntity<clientResponseDto> updateByCpf(
            @Parameter(description = "CPF do cliente (apenas números)", example = "12345678901")
            @PathVariable("cpf") String cpf,
            @RequestBody(description = "Dados atualizados do cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = clientWithAddressDto.class)))
            @org.springframework.web.bind.annotation.RequestBody @Valid clientWithAddressDto dto){
       clientResponseDto client = clientService.updateByCpf(cpf,dto);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping ("/{cpf}")
    @Operation(summary = "Excluir por CPF", description = "Exclui um cliente pelo CPF",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Excluído com sucesso", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
            })
    public ResponseEntity <clientWithAddressDto> deleteByCpf(
            @Parameter(description = "CPF do cliente (apenas números)", example = "12345678901")
            @PathVariable ("cpf") String cpf){
        clientService.deleteByCpf(cpf);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Lista clientes paginados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = clientResponseDto.class)))
            })
    public ResponseEntity <Page<clientResponseDto>> listClient(
            @Parameter(description = "Parâmetros de paginação padrão do Spring (page, size, sort)") Pageable pageable){
        Page <clientResponseDto> clientResponseDtoList = clientService.clientFindAll(pageable);
        return ResponseEntity.ok(clientResponseDtoList);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar por filtros", description = "Lista clientes por filtros informados, com paginação",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = clientWithAddressDto.class))))
            })
    public ResponseEntity <Page<clientResponseDto>> listClientByParameters(
            @Parameter(description = "Filtros de busca: name, cpf, email, phoneNumber, street, city, zipCode, state")
            @ModelAttribute ClienteFiltersDto filters ,
            @Parameter(description = "Parâmetros de paginação padrão do Spring (page, size, sort)") Pageable pageable){
        Page<clientResponseDto> clientWithAddressDtoList = clientService.clientByParameters(filters,pageable);
        return ResponseEntity.ok(clientWithAddressDtoList);
    }
}
