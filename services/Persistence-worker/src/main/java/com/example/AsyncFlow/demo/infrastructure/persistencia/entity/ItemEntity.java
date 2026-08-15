package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entidade JPA que representa um item da nota fiscal no banco de dados.
 * Tabela: itens (mesma usada pela nota-api)
 */
@Data
@Entity
@Table(name = "itens")
public class ItemEntity {

    @Id
    @Column(name = "serieNumber")
    private String serieNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_numero_nota")
    private NotaFiscalEntity notaFiscal;
}
