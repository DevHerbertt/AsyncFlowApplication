package com.example.AsyncFlow.demo.Domain.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Itens {

    private String serieNumber;
    private String name;
    private BigDecimal price;

}
