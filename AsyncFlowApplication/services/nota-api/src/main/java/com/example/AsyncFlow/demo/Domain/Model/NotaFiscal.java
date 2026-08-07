package com.example.AsyncFlow.demo.Domain.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class NotaFiscal {
    private String numeroNota;
    private Client client;
    private BigDecimal totalValue;
    private List<Itens> itens;
    private LocalDateTime buyDate;
    private String Cnpj;
    private NotaFiscalStatus status;




}
