package com.neoapp.cliente_api.service;

import com.neoapp.cliente_api.Specification.SpecificationClient;
import com.neoapp.cliente_api.controller.commonExceptions.ClientNotFoundException;
import com.neoapp.cliente_api.controller.commonExceptions.CpfAlreadyExistsException;
import com.neoapp.cliente_api.controller.dto.ClientFiltersDto;
import com.neoapp.cliente_api.controller.dto.ClientResponseDto;
import com.neoapp.cliente_api.controller.dto.ClientWithAddressDto;
import com.neoapp.cliente_api.model.Client;
import com.neoapp.cliente_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client saveClientWithAddress(ClientWithAddressDto cliDto) {
        if (clientRepository.existsByCpf(cliDto.cpf())) {
            throw new CpfAlreadyExistsException("O cpf:" + cliDto.cpf() + " já está cadastrado!");
        }
        var address = cliDto.mappedToAddress();
        var client = cliDto.mappedToClient();
        client.setAddress(address);
        return clientRepository.save(client);
    }

    @Transactional
    public ClientResponseDto findByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com o cpf: " + cpf + " não encontrado!"));
        return mapToResponseDto(client);
    }

    @Transactional
    public ClientResponseDto updateByCpf(String cpf, ClientWithAddressDto clientDto) {
        var address = clientDto.mappedToAddress();

        Client clientUpdate = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com o cpf: " + cpf + " não encontrado!"));

        clientUpdate.setCpf(clientDto.cpf());
        clientUpdate.setName(clientDto.name());
        clientUpdate.setBirthDate(clientDto.birthDate());
        clientUpdate.setEmail(clientDto.email());
        clientUpdate.setPhoneNumber(clientDto.phoneNumber());

        clientUpdate.getAddress().setCity(address.getCity());
        clientUpdate.getAddress().setState(address.getState());
        clientUpdate.getAddress().setStreet(address.getStreet());
        clientUpdate.getAddress().setZipCode(address.getZipCode());
        Client clientupdated = clientRepository.save(clientUpdate);

        return mapToResponseDto(clientupdated);
    }

    @Transactional
    public void deleteByCpf(String cpf) {
        if (!clientRepository.existsByCpf(cpf)) {
            throw new ClientNotFoundException("Cpf: " + cpf + " não encontrado!");
        }
        clientRepository.deleteByCpf(cpf);
    }

    @Transactional
    public Page<ClientResponseDto> clientFindAll(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(x -> mapToResponseDto(x));
    }

    @Transactional
    public Page<ClientResponseDto> clientByParameters(ClientFiltersDto filters, Pageable pageable) {
        Specification<Client> spec = SpecificationClient.clientSpecification(filters);
        Page<Client> clientsPage = clientRepository.findAll(spec, pageable);
        return clientsPage.map(x -> mapToResponseDto(x));
    }

    public ClientResponseDto mapToResponseDto(Client client) {
        int age = Period.between(client.getBirthDate(), LocalDate.now()).getYears();

        return new ClientResponseDto(client.getName(), client.getCpf(), client.getBirthDate(), client.getEmail(),
                client.getPhoneNumber(), client.getAddress().getStreet(), client.getAddress().getCity(),
                client.getAddress().getZipCode(), client.getAddress().getState(), age);
    }

}
