package com.example.AsyncFlow.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageRequest(
        @JsonProperty("notaFiscal") NotaFiscalDto notaFiscal
) {}
