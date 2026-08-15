package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Entidade JPA que representa o cliente no banco de dados.
 * Tabela: clientes (mesma usada pela nota-api)
 */
@Data
@Entity
@Table(name = "clientes")
public class ClientEntity {

    @Id
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cep")
    private String cep;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscalEntity> notasFiscais;
}
