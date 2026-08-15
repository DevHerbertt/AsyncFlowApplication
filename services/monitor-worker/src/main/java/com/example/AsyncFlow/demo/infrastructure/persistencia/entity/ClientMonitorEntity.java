package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidade JPA somente-leitura para o monitor.
 * Mapeia a tabela clientes criada pela nota-api / persistence-worker.
 */
@Data
@Entity
@Table(name = "clientes")
public class ClientMonitorEntity {

    @Id
    @Column(name = "cpf")
    private String cpf;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "cep")
    private String cep;
}
