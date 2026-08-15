package com.example.AsyncFlow.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemDto(
        @JsonProperty("serieNumber") String serieNumber,
        @JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price
) {}
