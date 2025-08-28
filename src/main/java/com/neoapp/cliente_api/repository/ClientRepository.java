package com.neoapp.cliente_api.repository;

import com.neoapp.cliente_api.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Page<Client> findAll(Pageable pageable);

    Optional<Client> findByCpf(String cpf);

    void deleteByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
