package com.neoapp.cliente_api.service;

import com.neoapp.cliente_api.controller.commonExceptions.ClientNotFoundException;
import com.neoapp.cliente_api.controller.commonExceptions.CpfAlreadyExistsException;
import com.neoapp.cliente_api.controller.dto.ClientFiltersDto;
import com.neoapp.cliente_api.controller.dto.ClientResponseDto;
import com.neoapp.cliente_api.controller.dto.ClientWithAddressDto;
import com.neoapp.cliente_api.model.Address;
import com.neoapp.cliente_api.model.Client;
import com.neoapp.cliente_api.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class ClientServiceTest {

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private ClientService clientService;

  private Client testClient;
  private Address testAddress;
  private ClientWithAddressDto testClientDto;
  private ClientResponseDto expectedResponseDto;

  @BeforeEach
  void setUp() {
    // Setup test data
    testAddress = new Address();
    testAddress.setId(1L);
    testAddress.setStreet("Rua das Flores, 123");
    testAddress.setCity("São Paulo");
    testAddress.setState("SP");
    testAddress.setZipCode("01234567");

    testClient = new Client();
    testClient.setId(1L);
    testClient.setName("João Silva");
    testClient.setCpf("12345678901");
    testClient.setBirthDate(LocalDate.of(1990, 5, 15));
    testClient.setEmail("joao.silva@email.com");
    testClient.setPhoneNumber("11987654321");
    testClient.setAddress(testAddress);

    testClientDto = new ClientWithAddressDto("João Silva", "12345678901", LocalDate.of(1990, 5, 15),
        "joao.silva@email.com", "11987654321", "Rua das Flores, 123", "São Paulo", "01234567", "SP");

    // Calculate age dynamically
    int age = java.time.Period.between(LocalDate.of(1990, 5, 15), LocalDate.now()).getYears();

    expectedResponseDto = new ClientResponseDto("João Silva", "12345678901", LocalDate.of(1990, 5, 15),
        "joao.silva@email.com", "11987654321", "Rua das Flores, 123", "São Paulo", "01234567", "SP", age // Age
                                                                                                         // calculated
                                                                                                         // from
                                                                                                         // 1990-05-15
                                                                                                         // to current
                                                                                                         // date
    );
  }

  @Test
  void testSaveClientWithAddress_Success() {
    // Given
    when(clientRepository.existsByCpf(testClientDto.cpf())).thenReturn(false);
    when(clientRepository.save(any(Client.class))).thenReturn(testClient);

    // When
    Client result = clientService.saveClientWithAddress(testClientDto);

    // Then
    assertNotNull(result);
    assertEquals(testClient.getName(), result.getName());
    assertEquals(testClient.getCpf(), result.getCpf());
    assertEquals(testClient.getEmail(), result.getEmail());
    assertEquals(testClient.getAddress().getCity(), result.getAddress().getCity());

    verify(clientRepository).existsByCpf(testClientDto.cpf());
    verify(clientRepository).save(any(Client.class));
  }

  @Test
  void testSaveClientWithAddress_CpfAlreadyExists() {
    // Given
    when(clientRepository.existsByCpf(testClientDto.cpf())).thenReturn(true);

    // When & Then
    CpfAlreadyExistsException exception = assertThrows(CpfAlreadyExistsException.class,
        () -> clientService.saveClientWithAddress(testClientDto));

    assertEquals("O cpf:12345678901 já está cadastrado!", exception.getMessage());
    verify(clientRepository).existsByCpf(testClientDto.cpf());
    verify(clientRepository, never()).save(any(Client.class));
  }

  @Test
  void testFindByCpf_Success() {
    // Given
    when(clientRepository.findByCpf("12345678901")).thenReturn(Optional.of(testClient));

    // When
    ClientResponseDto result = clientService.findByCpf("12345678901");

    // Then
    assertNotNull(result);
    assertEquals(expectedResponseDto.name(), result.name());
    assertEquals(expectedResponseDto.cpf(), result.cpf());
    assertEquals(expectedResponseDto.email(), result.email());
    assertEquals(expectedResponseDto.city(), result.city());

    verify(clientRepository).findByCpf("12345678901");
  }

  @Test
  void testFindByCpf_ClientNotFound() {
    // Given
    when(clientRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

    // When & Then
    ClientNotFoundException exception = assertThrows(ClientNotFoundException.class,
        () -> clientService.findByCpf("99999999999"));

    assertEquals("Cliente com o cpf: 99999999999 não encontrado!", exception.getMessage());
    verify(clientRepository).findByCpf("99999999999");
  }

  @Test
  void testUpdateByCpf_Success() {
    // Given
    ClientWithAddressDto updateDto = new ClientWithAddressDto("João Silva Atualizado", "12345678901",
        LocalDate.of(1990, 5, 15), "joao.atualizado@email.com", "11987654322", "Rua Nova, 456", "Rio de Janeiro",
        "20000000", "RJ");

    Client updatedClient = new Client();
    updatedClient.setId(1L);
    updatedClient.setName("João Silva Atualizado");
    updatedClient.setCpf("12345678901");
    updatedClient.setBirthDate(LocalDate.of(1990, 5, 15));
    updatedClient.setEmail("joao.atualizado@email.com");
    updatedClient.setPhoneNumber("11987654322");

    Address updatedAddress = new Address();
    updatedAddress.setId(1L);
    updatedAddress.setStreet("Rua Nova, 456");
    updatedAddress.setCity("Rio de Janeiro");
    updatedAddress.setState("RJ");
    updatedAddress.setZipCode("20000000");
    updatedClient.setAddress(updatedAddress);

    when(clientRepository.findByCpf("12345678901")).thenReturn(Optional.of(testClient));
    when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

    // When
    ClientResponseDto result = clientService.updateByCpf("12345678901", updateDto);

    // Then
    assertNotNull(result);
    assertEquals("João Silva Atualizado", result.name());
    assertEquals("joao.atualizado@email.com", result.email());
    assertEquals("Rio de Janeiro", result.city());
    assertEquals("RJ", result.state());

    verify(clientRepository).findByCpf("12345678901");
    verify(clientRepository).save(any(Client.class));
  }

  @Test
  void testUpdateByCpf_ClientNotFound() {
    // Given
    when(clientRepository.findByCpf("99999999999")).thenReturn(Optional.empty());

    // When & Then
    ClientNotFoundException exception = assertThrows(ClientNotFoundException.class,
        () -> clientService.updateByCpf("99999999999", testClientDto));

    assertEquals("Cliente com o cpf: 99999999999 não encontrado!", exception.getMessage());
    verify(clientRepository).findByCpf("99999999999");
    verify(clientRepository, never()).save(any(Client.class));
  }

  @Test
  void testDeleteByCpf_Success() {
    // Given
    when(clientRepository.existsByCpf("12345678901")).thenReturn(true);
    doNothing().when(clientRepository).deleteByCpf("12345678901");

    // When
    clientService.deleteByCpf("12345678901");

    // Then
    verify(clientRepository).existsByCpf("12345678901");
    verify(clientRepository).deleteByCpf("12345678901");
  }

  @Test
  void testDeleteByCpf_ClientNotFound() {
    // Given
    when(clientRepository.existsByCpf("99999999999")).thenReturn(false);

    // When & Then
    ClientNotFoundException exception = assertThrows(ClientNotFoundException.class,
        () -> clientService.deleteByCpf("99999999999"));

    assertEquals("Cpf: 99999999999 não encontrado!", exception.getMessage());
    verify(clientRepository).existsByCpf("99999999999");
    verify(clientRepository, never()).deleteByCpf(anyString());
  }

  @Test
  void testClientFindAll_Success() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Page<Client> clientPage = new PageImpl<>(List.of(testClient), pageable, 1);
    when(clientRepository.findAll(pageable)).thenReturn(clientPage);

    // When
    Page<ClientResponseDto> result = clientService.clientFindAll(pageable);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getContent().size());

    ClientResponseDto responseDto = result.getContent().get(0);
    assertEquals(expectedResponseDto.name(), responseDto.name());
    assertEquals(expectedResponseDto.cpf(), responseDto.cpf());

    verify(clientRepository).findAll(pageable);
  }

  @Test
  void testClientByParameters_Success() {
    // Given
    ClientFiltersDto filters = new ClientFiltersDto("João", null, null, null, null, null, null, null, null,
        Optional.empty());
    Pageable pageable = PageRequest.of(0, 10);
    Page<Client> clientPage = new PageImpl<>(List.of(testClient), pageable, 1);

    when(clientRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(clientPage);

    // When
    Page<ClientResponseDto> result = clientService.clientByParameters(filters, pageable);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getContent().size());

    ClientResponseDto responseDto = result.getContent().get(0);
    assertEquals(expectedResponseDto.name(), responseDto.name());
    assertEquals(expectedResponseDto.cpf(), responseDto.cpf());

    verify(clientRepository).findAll(any(Specification.class), eq(pageable));
  }

  @Test
  void testMapToResponseDto_Success() {
    // When
    ClientResponseDto result = clientService.mapToResponseDto(testClient);

    // Then
    assertNotNull(result);
    assertEquals(testClient.getName(), result.name());
    assertEquals(testClient.getCpf(), result.cpf());
    assertEquals(testClient.getBirthDate(), result.birthDate());
    assertEquals(testClient.getEmail(), result.email());
    assertEquals(testClient.getPhoneNumber(), result.phoneNumber());
    assertEquals(testClient.getAddress().getStreet(), result.street());
    assertEquals(testClient.getAddress().getCity(), result.city());
    assertEquals(testClient.getAddress().getZipCode(), result.zipCode());
    assertEquals(testClient.getAddress().getState(), result.state());

    // Verify age calculation (should be around 34-35 depending on current date)
    int calculatedAge = result.years();
    assertTrue(calculatedAge >= 34 && calculatedAge <= 35, "Age should be between 34-35, but was: " + calculatedAge);
  }

  @Test
  void testMapToResponseDto_AgeCalculation() {
    // Given - Create a client with a specific birth date for age testing
    Client youngClient = new Client();
    youngClient.setName("Young Person");
    youngClient.setCpf("98765432100");
    youngClient.setBirthDate(LocalDate.now().minusYears(25)); // 25 years old
    youngClient.setEmail("young@email.com");
    youngClient.setPhoneNumber("11999999999");

    Address youngAddress = new Address();
    youngAddress.setStreet("Young Street");
    youngAddress.setCity("Young City");
    youngAddress.setState("SP");
    youngAddress.setZipCode("12345678");
    youngClient.setAddress(youngAddress);

    // When
    ClientResponseDto result = clientService.mapToResponseDto(youngClient);

    // Then
    assertEquals(25, result.years());
  }

  @Test
  void testClientByParameters_EmptyFilters() {
    // Given
    ClientFiltersDto emptyFilters = new ClientFiltersDto(null, null, null, null, null, null, null, null, null,
        Optional.empty());
    Pageable pageable = PageRequest.of(0, 10);
    Page<Client> clientPage = new PageImpl<>(List.of(testClient), pageable, 1);

    when(clientRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(clientPage);

    // When
    Page<ClientResponseDto> result = clientService.clientByParameters(emptyFilters, pageable);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(clientRepository).findAll(any(Specification.class), eq(pageable));
  }

  @Test
  void testClientFindAll_EmptyPage() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Page<Client> emptyPage = new PageImpl<>(List.of(), pageable, 0);
    when(clientRepository.findAll(pageable)).thenReturn(emptyPage);

    // When
    Page<ClientResponseDto> result = clientService.clientFindAll(pageable);

    // Then
    assertNotNull(result);
    assertEquals(0, result.getTotalElements());
    assertTrue(result.getContent().isEmpty());
    verify(clientRepository).findAll(pageable);
  }
}
