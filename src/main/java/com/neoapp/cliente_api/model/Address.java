package com.neoapp.cliente_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column (name = "street" , nullable = false)
    private String street;
    @Column (name = "city" , nullable = false)
    private String  city;
    @Column (name = "state" , nullable = false)
    private String state;
    @Column (name = "zip_code" , nullable = false)
    private String zipCode;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private  Client client;
}
