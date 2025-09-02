package com.neoapp.cliente_api.Specification;


import com.neoapp.cliente_api.controller.dto.ClienteFiltersDto;
import com.neoapp.cliente_api.model.Client;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

 public class SpecificationClient {
     public static Specification<Client> clientSpecification(ClienteFiltersDto clientDto) {
         return (root, query, criteriaBuilder) -> {
             Join<Object, Object> addressJoin = root.join("address", JoinType.LEFT);

             List<Predicate> predicates = new ArrayList<>();

             // Use 'if' para cada campo opcional
             if (clientDto.name() != null && !clientDto.name().isEmpty()) {
                 predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + clientDto.name().toLowerCase() + "%"));
             }
             if (clientDto.cpf() != null && !clientDto.cpf().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(root.get("cpf"), clientDto.cpf()));
             }

             if (clientDto.email() != null && !clientDto.email().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(root.get("email"), clientDto.email()));
             }

             if (clientDto.birthDate() != null) {
                 predicates.add(criteriaBuilder.equal(root.get("birthDate"), clientDto.birthDate()));
             }

             if (clientDto.phoneNumber() != null && !clientDto.phoneNumber().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), clientDto.phoneNumber()));
             }
             if (clientDto.street() != null && !clientDto.street().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(addressJoin.get("street"), clientDto.street()));
             }
             if (clientDto.city() != null && !clientDto.city().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(addressJoin.get("city"), clientDto.city()));
             }
             if (clientDto.state() != null && !clientDto.state().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(addressJoin.get("state"), clientDto.state()));
             }
             if (clientDto.zipCode() != null && !clientDto.zipCode().isEmpty()) {
                 predicates.add(criteriaBuilder.equal(addressJoin.get("zipCode"), clientDto.zipCode()));
             }

             return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
         };
     }
 }