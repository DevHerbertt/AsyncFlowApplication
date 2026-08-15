package com.example.AsyncFlow.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientDto(
        @JsonProperty("cpf") String cpf,
        @JsonProperty("name") String name
) {}
