package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade JPA somente-leitura para o monitor.
 * Mapeia a tabela notas_ficais criada pelo persistence-worker / nota-api.
 */
@Data
@Entity
@Table(name = "notas_ficais")
public class NotaFiscalMonitorEntity {

    @Id
    @Column(name = "numero_nota")
    private String numeroNota;

    @Column(name = "client_cpf")
    private String clientCpf;

    @Column(name = "total_value")
    private BigDecimal totalValue;

    @Column(name = "buy_date")
    private LocalDateTime buyDate;

    @Column(name = "cnpj_emitente")
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotaFiscalStatus status;

    @Column(name = "s3_url")
    private String s3Url;
}
