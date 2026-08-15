package com.example.AsyncFlow.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record NotaFiscalDto(
        @JsonProperty("numeroNota")  String numeroNota,
        @JsonProperty("client")      ClientDto client,
        @JsonProperty("totalValue")  BigDecimal totalValue,
        @JsonProperty("itens")       List<ItemDto> itens,
        @JsonProperty("buyDate")     LocalDateTime buyDate,
        @JsonProperty("cnpj")        String cnpj,
        @JsonProperty("status")      String status,
        @JsonProperty("fileBase64")  String fileBase64,
        @JsonProperty("fileName")    String fileName
) {}
