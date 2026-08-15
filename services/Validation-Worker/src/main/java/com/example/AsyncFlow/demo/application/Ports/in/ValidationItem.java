package com.example.AsyncFlow.demo.application.Ports.in;

import com.example.AsyncFlow.demo.Domain.Model.Item;

import java.math.BigDecimal;

public interface ValidationItem {

    boolean validation(Item item);

    boolean isValidSerieNumber(String serieNumber);

    boolean isValidName(String name);

    boolean isValidPrice(BigDecimal price);
}