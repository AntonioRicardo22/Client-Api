package com.example.clientapi.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    private ClientRepository clientRepository;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientRepository = Mockito.mock(ClientRepository.class);
        clientService = new ClientService(clientRepository);
    }

    @Test
    void create_shouldPersist() {
        Client toSave = Client.builder().name("Alice").email("alice@example.com").build();
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Client saved = clientService.create(toSave);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("Alice");
    }

    @Test
    void getById_whenMissing_shouldThrow() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class, () -> clientService.getById(99L));
    }

    @Test
    void list_shouldReturnAll() {
        when(clientRepository.findAll()).thenReturn(List.of(
                Client.builder().id(1L).name("A").email("a@a.com").build(),
                Client.builder().id(2L).name("B").email("b@b.com").build()
        ));

        List<Client> clients = clientService.list();
        assertThat(clients).hasSize(2);
    }
}

