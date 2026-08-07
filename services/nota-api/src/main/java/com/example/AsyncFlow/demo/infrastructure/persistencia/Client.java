package com.example.AsyncFlow.demo.infrastructure.persistencia;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "clientes")
public class Client {

    @Column(name = "email")
    @Email(message = "Email invalido")
    private String email;

    @Id
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "name")
    private String name;

    @Column(name = "cep")
    private String cep;

    @Column(name = "password")
    private String password;

    @Column(name = "notaFiscal")
    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<NotaFiscal> notaFiscal;


}
