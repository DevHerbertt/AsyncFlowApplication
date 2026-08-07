package com.example.AsyncFlow.demo.infrastructure.persistencia;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "notas_ficais")
public class NotaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero_nota", nullable = false, unique = true)
    private String numeroNota;

    @ManyToOne()
    @JoinColumn(name = "client_cpf")
    private Client client;

    private BigDecimal totalValue;

    @OneToMany(
            mappedBy = "notaFiscal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "itens_serieNumber")
    private List<Itens> itens;

    private LocalDateTime buyDate;

    @CNPJ
    @Column(name = "cnpj_emitente")
    private String Cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotaFiscalStatus status;




}
