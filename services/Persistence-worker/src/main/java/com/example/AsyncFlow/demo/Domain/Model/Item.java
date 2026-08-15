package com.example.AsyncFlow.demo.Domain.Model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {
    private String serieNumber;
    private String name;
    private BigDecimal price;
}
