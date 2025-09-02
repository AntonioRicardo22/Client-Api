package com.neoapp.cliente_api.service;
import com.neoapp.cliente_api.controller.commonExceptions.clientNotFoundExceptions;
import com.neoapp.cliente_api.controller.commonExceptions.cpfAlreadyExistsException;
import com.neoapp.cliente_api.controller.dto.ClienteFiltersDto;
import com.neoapp.cliente_api.controller.dto.clientResponseDto;
import com.neoapp.cliente_api.controller.dto.clientWithAddressDto;
import com.neoapp.cliente_api.model.Client;
import com.neoapp.cliente_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service
public class clientService {

    private static Logger logger = LoggerFactory.getLogger(clientService.class);
    private final ClientRepository clientRepository;

    @Transactional
    public Client saveClientWithAddress(clientWithAddressDto cliDto){
        if(clientRepository.existsByCpf(cliDto.cpf())){
            throw new cpfAlreadyExistsException("O cpf:" + cliDto.cpf()+ " já está cadastrado!");
        }
        var address = cliDto.mappedToAddress();
        var client = cliDto.mappedToClient();
        client.setAddress(address);
        return clientRepository.save(client);
    }

    @Transactional
    public  clientResponseDto findByCpf(String cpf) {
       Client client = clientRepository.findByCpf(cpf)
               .orElseThrow(() -> new clientNotFoundExceptions ("Cliente com o cpf: " + cpf +" não encontrado!"));
     return   mapToResponseDto(client);
    }

    @Transactional
    public clientResponseDto updateByCpf(String cpf , clientWithAddressDto clientDto){
        var address = clientDto.mappedToAddress();

        Client clientupdate = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new clientNotFoundExceptions ("Cliente com o cpf: " + cpf +" não encontrado!"));

        clientupdate.setCpf(clientDto.cpf());
        clientupdate.setName(clientDto.name());
        clientupdate.setBirthDate(clientDto.birthDate());
        clientupdate.setEmail(clientDto.email());
        clientupdate.setPhoneNumber(clientDto.phoneNumber());

        clientupdate.getAddress().setCity(address.getCity());
        clientupdate.getAddress().setState(address.getState());
        clientupdate.getAddress().setStreet(address.getStreet());
        clientupdate.getAddress().setZipCode(address.getZipCode());
        Client clientupdated  = clientRepository.save(clientupdate);

        return mapToResponseDto (clientupdated);
    }

    @Transactional
    public void deleteByCpf (String cpf){
        Optional<Client> byId = clientRepository.findByCpf(cpf);

        if(!clientRepository.existsByCpf(cpf)){
            throw new clientNotFoundExceptions("Cpf: " + cpf +" não encontrado!");
        }
        clientRepository.deleteByCpf(cpf);
    }

    @Transactional
    public Page <clientResponseDto> clientFindAll(Pageable pageable){
        Page <Client> clients = clientRepository.findAll(pageable);
       /* Page <clientResponseDto> clientResponseDtoList = clients.
                map( x ->   new clientResponseDto(x.getName(), x.getCpf(), x.getBirthDate(),
                        x.getEmail()
                        ,x.getPhoneNumber(),
                        x.getAddress().getStreet(),x.getAddress().getCity(),
                        x.getAddress().getZipCode(),x.getAddress().getState(),calculateAge(x)));*/
       return clients.map(x -> mapToResponseDto(x));

       // return  clientResponseDtoList;
    }

    @Transactional
    public Page<clientResponseDto> clientByParameters(ClienteFiltersDto filters, Pageable pageable) {
        Page<Client> clientsPage = clientRepository.findByCustomCriteria(
            filters.name(),
            filters.cpf(),
            filters.email(),
            filters.phoneNumber(),
            filters.street(),
            filters.city(),
            filters.zipCode(),
            filters.state(),
            pageable
        );
        logger.info("clientsPage: " + clientsPage);
        return clientsPage.map(x ->  mapToResponseDto(x));
    }


    public  clientResponseDto mapToResponseDto(Client client){
        int age = Period.between(client.getBirthDate(),LocalDate.now()).getYears();

        return new clientResponseDto(
                client.getName(),
                client.getCpf(), 
                client.getBirthDate(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getAddress().getStreet(),
                client.getAddress().getCity(),
                client.getAddress().getZipCode(),
                client.getAddress().getState(),
                age

        );
    }

}
