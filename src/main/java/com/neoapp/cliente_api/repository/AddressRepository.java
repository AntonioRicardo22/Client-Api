package com.neoapp.cliente_api.repository;

import com.neoapp.cliente_api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository <Address,Long> {


}
