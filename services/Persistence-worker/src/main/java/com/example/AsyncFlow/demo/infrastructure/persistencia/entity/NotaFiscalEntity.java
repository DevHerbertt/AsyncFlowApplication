package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade JPA que representa a nota fiscal no banco de dados.
 * Tabela: notas_ficais (mesma usada pela nota-api)
 */
@Data
@Entity
@Table(name = "notas_ficais")
public class NotaFiscalEntity {

    @Id
    @Column(name = "numero_nota", nullable = false, unique = true)
    private String numeroNota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_cpf")
    private ClientEntity client;

    @Column(name = "totalValue")
    private BigDecimal totalValue;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> itens;

    @Column(name = "buyDate")
    private LocalDateTime buyDate;

    @Column(name = "cnpj_emitente")
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotaFiscalStatus status;

    /**
     * URL do arquivo XML/PDF armazenado no S3.
     * Preenchido pelo persistence-worker após o upload.
     */
    @Column(name = "s3_url")
    private String s3Url;
}
