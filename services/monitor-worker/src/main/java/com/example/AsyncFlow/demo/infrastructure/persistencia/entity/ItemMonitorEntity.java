package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entidade JPA somente-leitura para o monitor.
 * Mapeia a tabela itens criada pela nota-api / persistence-worker.
 */
@Data
@Entity
@Table(name = "itens")
public class ItemMonitorEntity {

    @Id
    @Column(name = "serieNumber")
    private String serieNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "nota_fiscal_numero_nota")
    private String notaFiscalNumeroNota;
}
