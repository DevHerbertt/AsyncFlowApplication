package com.example.AsyncFlow.demo.application.Ports.DTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public class ItensDTO {

    private String serieNumber;
    private String name;
    private BigDecimal price;
}
