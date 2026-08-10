package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.application.Ports.in.ValidationItem;

import java.math.BigDecimal;

public class ValidationItemUsercase implements ValidationItem {

    @Override
    public boolean validation(Item item) {
        return isValidSerieNumber(item.getSerieNumber())
                && isValidName(item.getName())
                && isValidPrice(item.getPrice());
    }

    /**
     * Valida o número de série do item.
     * Deve ser não nulo, não vazio e não conter apenas espaços em branco.
     */
    @Override
    public boolean isValidSerieNumber(String serieNumber) {
        return serieNumber != null && !serieNumber.trim().isEmpty();
    }

    /**
     * Valida o nome do item.
     * Deve ser não nulo, não vazio e não conter apenas espaços em branco.
     */
    @Override
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Valida o preço do item.
     * Deve ser não nulo e maior que zero.
     */
    @Override
    public boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
}