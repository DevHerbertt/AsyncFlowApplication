package com.example.AsyncFlow.demo.Domain.Model;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class NotaFiscal {
    private String numeroNota;
    private Client client;
    private BigDecimal totalValue;
    private List<Item> itens;
    private LocalDateTime buyDate;
    private String Cnpj;
    private NotaFiscalStatus status;
}
