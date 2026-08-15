package com.example.AsyncFlow.demo.Domain.Model;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotaFiscal {
    private String numeroNota; //ID da nota
    private Client client;
    private BigDecimal totalValue;
    private List<Item> itens;
    private LocalDateTime buyDate;
    private String cnpj;
    private NotaFiscalStatus status;
    // Campos opcionais enviados pelo script de teste / persistence-worker
    private String fileBase64;
    private String fileName;
}
