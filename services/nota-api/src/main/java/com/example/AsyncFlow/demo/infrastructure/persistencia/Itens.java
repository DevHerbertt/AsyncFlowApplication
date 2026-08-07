package com.example.AsyncFlow.demo.infrastructure.persistencia;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "itens")
public class Itens {

    @Id
    private String serieNumber;

    @NotBlank
    private String name;

    @NotEmpty
    private BigDecimal price;

}
