package com.example.AsyncFlow.demo.application.Ports.DTO;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class NotaFiscalDto {

    private String numeroNota;
    private Client client;
    private BigDecimal totalValue;
    private List<ItensDTO> itens;
    private LocalDateTime buyDate;
    private String Cnpj;
    private NotaFiscalStatus status;
}
