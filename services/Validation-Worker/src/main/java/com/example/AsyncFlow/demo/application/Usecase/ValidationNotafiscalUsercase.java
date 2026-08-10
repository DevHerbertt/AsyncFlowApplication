package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.ValidationNotafiscalService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ValidationNotafiscalUsercase implements ValidationNotafiscalService {

    private final ValidationItemUsercase validationItemUsercase = new ValidationItemUsercase();

    @Override
    public boolean validation(NotaFiscal notaFiscal) {
        return isValidNumeroNota(notaFiscal.getNumeroNota())
                && isValidTotalValue(notaFiscal.getTotalValue())
                && isValidBuyDate(notaFiscal.getBuyDate())
                && isValidCnpj(notaFiscal.getCnpj())
                && isValidStatus(notaFiscal.getStatus())
                && isValidItens(notaFiscal.getItens());
    }

    /**
     * Valida a lista de itens da nota fiscal.
     * Deve ser não nula, não vazia e todos os itens devem ser válidos.
     */
    @Override
    public boolean isValidItens(List<Item> itens) {
        if (itens == null || itens.isEmpty()) {
            return false;
        }

        for (Item item : itens) {
            if (!validationItemUsercase.validation(item)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Valida o número da nota fiscal.
     * Deve ser não nulo, não vazio e não conter apenas espaços em branco.
     */
    @Override
    public boolean isValidNumeroNota(String numeroNota) {
        return numeroNota != null && !numeroNota.trim().isEmpty();
    }

    /**
     * Valida o valor total da nota fiscal.
     * Deve ser não nulo e maior que zero.
     */
    @Override
    public boolean isValidTotalValue(BigDecimal totalValue) {
        return totalValue != null && totalValue.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida a data de compra da nota fiscal.
     * Deve ser não nula e não pode ser uma data futura.
     */
    @Override
    public boolean isValidBuyDate(LocalDateTime buyDate) {
        return buyDate != null && !buyDate.isAfter(LocalDateTime.now());
    }

    /**
     * Valida o CNPJ da nota fiscal.
     * Deve ser não nulo, conter exatamente 14 dígitos e ter dígitos verificadores válidos.
     */
    @Override
    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        // Remove caracteres não numéricos (pontos, barras e traços)
        String cnpjLimpo = cnpj.replaceAll("\\D", "");

        // Deve ter exatamente 14 dígitos
        if (cnpjLimpo.length() != 14) {
            return false;
        }

        // Rejeita CNPJs com todos os dígitos iguais (ex: 00.000.000/0000-00)
        if (cnpjLimpo.chars().distinct().count() == 1) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int[] pesosPrimeiroDigito = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int somaPrimeiro = 0;
        for (int i = 0; i < 12; i++) {
            somaPrimeiro += Character.getNumericValue(cnpjLimpo.charAt(i)) * pesosPrimeiroDigito[i];
        }
        int restoPrimeiro = somaPrimeiro % 11;
        int primeiroDigito = (restoPrimeiro < 2) ? 0 : (11 - restoPrimeiro);

        if (primeiroDigito != Character.getNumericValue(cnpjLimpo.charAt(12))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int[] pesosSegundoDigito = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int somaSegundo = 0;
        for (int i = 0; i < 13; i++) {
            somaSegundo += Character.getNumericValue(cnpjLimpo.charAt(i)) * pesosSegundoDigito[i];
        }
        int restoSegundo = somaSegundo % 11;
        int segundoDigito = (restoSegundo < 2) ? 0 : (11 - restoSegundo);

        return segundoDigito == Character.getNumericValue(cnpjLimpo.charAt(13));
    }

    /**
     * Valida o status da nota fiscal.
     * Deve ser não nulo.
     */
    @Override
    public boolean isValidStatus(NotaFiscalStatus status) {
        return status != null;
    }
}