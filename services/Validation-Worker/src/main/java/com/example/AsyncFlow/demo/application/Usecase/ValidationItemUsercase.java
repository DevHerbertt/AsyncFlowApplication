package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.application.Ports.in.ValidationItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ValidationItemUsercase implements ValidationItem {

    private static final Logger log = LoggerFactory.getLogger(ValidationItemUsercase.class);

    @Override
    public boolean validation(Item item) {
        boolean serieValida = isValidSerieNumber(item.getSerieNumber());
        boolean nomeValido = isValidName(item.getName());
        boolean precoValido = isValidPrice(item.getPrice());

        boolean resultado = serieValida && nomeValido && precoValido;
        log.debug("     Item série='{}', nome='{}', preço={}: {}",
                item.getSerieNumber(), item.getName(), item.getPrice(),
                resultado ? "✅" : "❌");

        return resultado;
    }

    @Override
    public boolean isValidSerieNumber(String serieNumber) {
        boolean valido = serieNumber != null && !serieNumber.trim().isEmpty();
        if (!valido) {
            log.warn("       ⚠️ Número de série inválido: '{}'", serieNumber);
        }
        return valido;
    }

    @Override
    public boolean isValidName(String name) {
        boolean valido = name != null && !name.trim().isEmpty();
        if (!valido) {
            log.warn("       ⚠️ Nome do item inválido: '{}'", name);
        }
        return valido;
    }

    @Override
    public boolean isValidPrice(BigDecimal price) {
        boolean valido = price != null && price.compareTo(BigDecimal.ZERO) > 0;
        if (!valido) {
            log.warn("       ⚠️ Preço inválido: {}", price);
        }
        return valido;
    }
}