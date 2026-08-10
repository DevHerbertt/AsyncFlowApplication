package com.example.AsyncFlow.demo.application.Ports;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ValidationNotafiscalService {

    boolean validation(NotaFiscal notaFiscal);

    boolean isValidNumeroNota(String numeroNota);

    boolean isValidTotalValue(BigDecimal totalValue);

    boolean isValidBuyDate(LocalDateTime buyDate);

    boolean isValidCnpj(String cnpj);

    boolean isValidStatus(NotaFiscalStatus status);

    boolean isValidItens(List<Item> itens);
}
