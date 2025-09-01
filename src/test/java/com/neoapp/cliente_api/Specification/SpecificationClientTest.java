package com.neoapp.cliente_api.Specification;

import com.neoapp.cliente_api.controller.dto.ClientFiltersDto;

import com.neoapp.cliente_api.model.Client;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class SpecificationClientTest {

    @Mock
    private Root<Client> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Join<Object, Object> addressJoin;

    @Mock
    private Path<Object> path;

    @Mock
    private Predicate predicate;

    @Mock
    private Expression<String> expression;

    @BeforeEach
    void setUp() {
        // Setup common mocks
        when(root.join("address", JoinType.LEFT)).thenReturn(addressJoin);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        // Setup lenient stubbing for addressJoin.get() to handle all possible field
        // names
        lenient().when(addressJoin.get(anyString())).thenReturn(path);
    }

    @Test
    void testClientSpecification_WithNameFilter() {
        // Given
        String name = "João";
        ClientFiltersDto filters = new ClientFiltersDto(
                name, null, null, null, null, null, null, null, null, Optional.empty());

        when(root.get("name")).thenReturn(path);
        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(expression);
        when(criteriaBuilder.like(expression, "%" + name.toLowerCase() + "%")).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).join("address", JoinType.LEFT);
        verify(criteriaBuilder).lower(any(Expression.class));
        verify(criteriaBuilder).like(expression, "%" + name.toLowerCase() + "%");
        verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
    void testClientSpecification_WithCpfFilter() {
        // Given
        String cpf = "12345678901";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, cpf, null, null, null, null, null, null, null, Optional.empty());

        when(root.get("cpf")).thenReturn(path);
        when(criteriaBuilder.equal(path, cpf)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).get("cpf");
        verify(criteriaBuilder).equal(path, cpf);
    }

    @Test
    void testClientSpecification_WithEmailFilter() {
        // Given
        String email = "joao@example.com";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, email, null, null, null, null, null, Optional.empty());

        when(root.get("email")).thenReturn(path);
        when(criteriaBuilder.equal(path, email)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).get("email");
        verify(criteriaBuilder).equal(path, email);
    }

    @Test
    void testClientSpecification_WithBirthDateFilter() {
        // Given
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, birthDate, null, null, null, null, null, null, Optional.empty());

        when(root.get("birthDate")).thenReturn(path);
        when(criteriaBuilder.equal(path, birthDate)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).get("birthDate");
        verify(criteriaBuilder).equal(path, birthDate);
    }

    @Test
    void testClientSpecification_WithPhoneNumberFilter() {
        // Given
        String phoneNumber = "11987654321";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, phoneNumber, null, null, null, null, Optional.empty());

        when(root.get("phoneNumber")).thenReturn(path);
        when(criteriaBuilder.equal(path, phoneNumber)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).get("phoneNumber");
        verify(criteriaBuilder).equal(path, phoneNumber);
    }

    @Test
    void testClientSpecification_WithStreetFilter() {
        // Given
        String street = "Rua das Flores";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, street, null, null, null, Optional.empty());

        when(criteriaBuilder.equal(path, street)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(addressJoin, atLeastOnce()).get(anyString());
        verify(criteriaBuilder).equal(path, street);
    }

    @Test
    void testClientSpecification_WithCityFilter() {
        // Given
        String city = "São Paulo";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, null, city, null, null, Optional.empty());

        when(criteriaBuilder.equal(path, city)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(addressJoin, atLeastOnce()).get(anyString());
        verify(criteriaBuilder).equal(path, city);
    }

    @Test
    void testClientSpecification_WithStateFilter() {
        // Given
        String state = "SP";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, null, null, state, null, Optional.empty());

        when(criteriaBuilder.equal(path, state)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(addressJoin, atLeastOnce()).get(anyString());
        verify(criteriaBuilder).equal(path, state);
    }

    @Test
    void testClientSpecification_WithZipCodeFilter() {
        // Given
        String zipCode = "01310930";
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, null, null, null, zipCode, Optional.empty());

        when(criteriaBuilder.equal(path, zipCode)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(addressJoin, atLeastOnce()).get(anyString());
        verify(criteriaBuilder).equal(path, zipCode);
    }

    @Test
    void testClientSpecification_WithMultipleFilters() {
        // Given
        String name = "Maria";
        String cpf = "98765432100";
        String email = "maria@example.com";
        LocalDate birthDate = LocalDate.of(1985, 3, 20);
        String phoneNumber = "11999887766";
        String street = "Avenida Paulista";
        String city = "São Paulo";
        String state = "SP";
        String zipCode = "01310100";

        ClientFiltersDto filters = new ClientFiltersDto(
                name, cpf, birthDate, email, phoneNumber, street, city, state, zipCode, Optional.empty());

        // Setup mocks for all fields
        when(root.get("name")).thenReturn(path);
        when(root.get("cpf")).thenReturn(path);
        when(root.get("email")).thenReturn(path);
        when(root.get("birthDate")).thenReturn(path);
        when(root.get("phoneNumber")).thenReturn(path);

        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(expression);
        when(criteriaBuilder.like(expression, "%" + name.toLowerCase() + "%")).thenReturn(predicate);
        when(criteriaBuilder.equal(path, cpf)).thenReturn(predicate);
        when(criteriaBuilder.equal(path, email)).thenReturn(predicate);
        when(criteriaBuilder.equal(path, birthDate)).thenReturn(predicate);
        when(criteriaBuilder.equal(path, phoneNumber)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(criteriaBuilder).and(any(Predicate[].class));
        // Verify that all fields are accessed
        verify(root).get("name");
        verify(root).get("cpf");
        verify(root).get("email");
        verify(root).get("birthDate");
        verify(root).get("phoneNumber");
        verify(addressJoin, atLeastOnce()).get("street");
        verify(addressJoin, atLeastOnce()).get("city");
        verify(addressJoin, atLeastOnce()).get("state");
        verify(addressJoin, atLeastOnce()).get("zipCode");
    }

    @Test
    void testClientSpecification_WithNullValues() {
        // Given
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, null, null, null, null, Optional.empty());

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).join("address", JoinType.LEFT);
        verify(criteriaBuilder).and(any(Predicate[].class));
        // Verify that no individual field predicates are created
        verify(root, never()).get(anyString());
        verify(addressJoin, never()).get(anyString());
    }

    @Test
    void testClientSpecification_WithEmptyStringValues() {
        // Given
        ClientFiltersDto filters = new ClientFiltersDto(
                "", "", null, "", "", "", "", "", "", Optional.empty());

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).join("address", JoinType.LEFT);
        verify(criteriaBuilder).and(any(Predicate[].class));
        // Verify that no individual field predicates are created for empty strings
        verify(root, never()).get(anyString());
        verify(addressJoin, never()).get(anyString());
    }

    @Test
    void testClientSpecification_NameCaseInsensitive() {
        // Given
        String name = "JOAO";
        ClientFiltersDto filters = new ClientFiltersDto(
                name, null, null, null, null, null, null, null, null, Optional.empty());

        when(root.get("name")).thenReturn(path);
        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(expression);
        when(criteriaBuilder.like(expression, "%" + name.toLowerCase() + "%")).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(criteriaBuilder).lower(any(Expression.class));
        verify(criteriaBuilder).like(expression, "%joao%");
    }

    @Test
    void testClientSpecification_WithPartialFilters() {
        // Given - only some fields filled
        String name = "Pedro";
        String city = "Rio de Janeiro";
        String state = "RJ";

        ClientFiltersDto filters = new ClientFiltersDto(
                name, null, null, null, null, null, city, state, null, Optional.empty());

        when(root.get("name")).thenReturn(path);

        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(expression);
        when(criteriaBuilder.like(expression, "%" + name.toLowerCase() + "%")).thenReturn(predicate);
        when(criteriaBuilder.equal(path, city)).thenReturn(predicate);
        when(criteriaBuilder.equal(path, state)).thenReturn(predicate);

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Then
        assertNotNull(result);
        verify(root).get("name");
        verify(addressJoin, atLeastOnce()).get(anyString());
        verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
    void testClientSpecification_AddressJoinType() {
        // Given
        ClientFiltersDto filters = new ClientFiltersDto(
                null, null, null, null, null, null, null, null, null, Optional.empty());

        // When
        Specification<Client> specification = SpecificationClient.clientSpecification(filters);
        specification.toPredicate(root, query, criteriaBuilder);

        // Then
        verify(root).join("address", JoinType.LEFT);
    }
}