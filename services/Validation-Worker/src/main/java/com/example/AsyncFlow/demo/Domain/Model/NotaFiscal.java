package com.example.AsyncFlow.demo.Domain.Model;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotaFiscal {
    private String numeroNota; //ID da nota
    private Client client;
    private BigDecimal totalValue;
    private List<Item> itens;
    private LocalDateTime buyDate;
    private String Cnpj;
    private NotaFiscalStatus status;
}
