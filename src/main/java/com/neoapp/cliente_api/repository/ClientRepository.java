package com.neoapp.cliente_api.repository;

import com.neoapp.cliente_api.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository <Client,Long > {

    Page <Client> findAll(Pageable pageable);

    // Uso do JPQL - para a busca customizada por diferentes campos
    @Query("""
    SELECT c FROM Client c LEFT JOIN FETCH c.address a
    WHERE c.name LIKE CONCAT('%', COALESCE(:name, ''), '%')
    AND c.cpf = COALESCE(:cpf, c.cpf)
    AND c.email = COALESCE(:email, c.email)
    AND a.street LIKE CONCAT('%', COALESCE(:street, ''), '%')
    AND a.city = COALESCE(:city, a.city)
    AND a.zipCode = COALESCE(:zipCode, a.zipCode)
    AND a.state = COALESCE(:state, a.state)
    """)
    Page<Client> findByCustomCriteria(
            @Param("name") String name,
            @Param("cpf") String cpf,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            @Param("street") String street,
            @Param("city") String city,
            @Param("zipCode") String zipCode,
            @Param("state") String state,
            Pageable pageable
    );

    // uso do QueryMethos para busca através do cpf
    Optional<Client> findByCpf(String cpf);
    void deleteByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
   /* @Query("""
        SELECT c FROM Client c LEFT JOIN FETCH c.address a
        WHERE c.name LIKE CONCAT('%', COALESCE(:name, ''), '%')
        AND c.cpf = COALESCE(:cpf, c.cpf)
        AND c.birthDate = COALESCE(:birthDate, c.birthDate)
        AND c.email = COALESCE(:email, c.email)
        AND c.phoneNumber = COALESCE(:phoneNumber, c.phoneNumber)
        AND a.street LIKE CONCAT('%', COALESCE(:street, ''), '%')
        AND a.city = COALESCE(:city, a.city)
        AND a.zipCode = COALESCE(:zipCode, a.zipCode)
        AND a.state = COALESCE(:state, a.state)
        """)*/