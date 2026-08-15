package com.example.AsyncFlow.demo.Domain.Model;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotaFiscal {
    private String numeroNota;
    private Client client;
    private BigDecimal totalValue;
    private List<Item> itens;
    private LocalDateTime buyDate;
    private String cnpj;
    private NotaFiscalStatus status;
    // Conteúdo do arquivo XML/PDF em Base64 (opcional, enviado pelo validation-worker)
    private String fileBase64;
    // Nome do arquivo (ex: nf-12345.xml ou nf-12345.pdf)
    private String fileName;
}
