package com.example.AsyncFlow.demo.infrastructure.persistencia.entity;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nota_fiscal")
public class NotaFiscalEntity {

    @Id
    private String numeroNota;

    @Enumerated(EnumType.STRING)
    private NotaFiscalStatus status;
}
