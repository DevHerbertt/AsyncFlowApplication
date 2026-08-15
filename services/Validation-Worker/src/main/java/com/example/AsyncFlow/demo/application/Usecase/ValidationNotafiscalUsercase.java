package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.in.ValidationItem;
import com.example.AsyncFlow.demo.application.Ports.in.ValidationNotafiscalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValidationNotafiscalUsercase implements ValidationNotafiscalService {

    private static final Logger log = LoggerFactory.getLogger(ValidationNotafiscalUsercase.class);

    private final ValidationItem validationItem;

    public ValidationNotafiscalUsercase(ValidationItem validationItem) {
        this.validationItem = validationItem;
    }

    @Override
    public boolean validation(NotaFiscal notaFiscal) {
        log.info("🔍 ValidationNotafiscalUsercase.validation() INICIADO");

        boolean numeroNotaValido = isValidNumeroNota(notaFiscal.getNumeroNota());
        log.info("   Validação numeroNota '{}': {}", notaFiscal.getNumeroNota(), numeroNotaValido ? "✅" : "❌");

        boolean totalValueValido = isValidTotalValue(notaFiscal.getTotalValue());
        log.info("   Validação totalValue '{}': {}", notaFiscal.getTotalValue(), totalValueValido ? "✅" : "❌");

        boolean buyDateValido = isValidBuyDate(notaFiscal.getBuyDate());
        log.info("   Validação buyDate '{}': {}", notaFiscal.getBuyDate(), buyDateValido ? "✅" : "❌");

        boolean cnpjValido = isValidCnpj(notaFiscal.getCnpj());
        log.info("   Validação CNPJ '{}': {}", notaFiscal.getCnpj(), cnpjValido ? "✅" : "❌");

        boolean statusValido = isValidStatus(notaFiscal.getStatus());
        log.info("   Validação status '{}': {}", notaFiscal.getStatus(), statusValido ? "✅" : "❌");

        boolean itensValido = isValidItens(notaFiscal.getItens());
        log.info("   Validação itens ({} itens): {}", notaFiscal.getItens() != null ? notaFiscal.getItens().size() : 0, itensValido ? "✅" : "❌");

        boolean resultado = numeroNotaValido && totalValueValido && buyDateValido && cnpjValido && statusValido && itensValido;
        log.info("🏁 Validação geral: {}", resultado ? "✅ VÁLIDA" : "❌ INVÁLIDA");

        return resultado;
    }

    @Override
    public boolean isValidItens(List<Item> itens) {
        if (itens == null || itens.isEmpty()) {
            log.warn("   ⚠️ Lista de itens está nula ou vazia!");
            return false;
        }

        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            log.debug("   Validando item[{}]: série={}, nome={}, preço={}", i, item.getSerieNumber(), item.getName(), item.getPrice());
            if (!validationItem.validation(item)) {
                log.warn("   ⚠️ Item[{}] INVÁLIDO: série={}, nome={}, preço={}", i, item.getSerieNumber(), item.getName(), item.getPrice());
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isValidNumeroNota(String numeroNota) {
        boolean valido = numeroNota != null && !numeroNota.trim().isEmpty();
        if (!valido) {
            log.warn("   ⚠️ Número da nota é nulo ou vazio!");
        }
        return valido;
    }

    @Override
    public boolean isValidTotalValue(BigDecimal totalValue) {
        boolean valido = totalValue != null && totalValue.compareTo(BigDecimal.ZERO) > 0;
        if (!valido) {
            log.warn("   ⚠️ TotalValue inválido: {}", totalValue);
        }
        return valido;
    }

    @Override
    public boolean isValidBuyDate(LocalDateTime buyDate) {
        boolean valido = buyDate != null && !buyDate.isAfter(LocalDateTime.now());
        if (!valido) {
            log.warn("   ⚠️ BuyDate inválido: {}", buyDate);
        }
        return valido;
    }

    @Override
    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null) {
            log.warn("   ⚠️ CNPJ é nulo!");
            return false;
        }

        // Remove caracteres não numéricos (pontos, barras e traços)
        String cnpjLimpo = cnpj.replaceAll("\\D", "");

        // Deve ter exatamente 14 dígitos
        if (cnpjLimpo.length() != 14) {
            log.warn("   ⚠️ CNPJ inválido: '{}' tem {} dígitos (deveria ter 14)", cnpjLimpo, cnpjLimpo.length());
            return false;
        }

        // Rejeita CNPJs com todos os dígitos iguais (ex: 00.000.000/0000-00)
        if (cnpjLimpo.chars().distinct().count() == 1) {
            log.warn("   ⚠️ CNPJ com todos os dígitos iguais: '{}'", cnpjLimpo);
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
            log.warn("   ⚠️ CNPJ '{}': primeiro dígito verificador inválido (esperado {}, calculado {})",
                    cnpj, primeiroDigito, Character.getNumericValue(cnpjLimpo.charAt(12)));
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

        if (segundoDigito != Character.getNumericValue(cnpjLimpo.charAt(13))) {
            log.warn("   ⚠️ CNPJ '{}': segundo dígito verificador inválido (esperado {}, calculado {})",
                    cnpj, segundoDigito, Character.getNumericValue(cnpjLimpo.charAt(13)));
            return false;
        }

        // Se chegou até aqui, CNPJ é válido
        log.debug("   ✅ CNPJ '{}' válido (dígitos verificadores OK)", cnpj);
        return true;
    }

    @Override
    public boolean isValidStatus(NotaFiscalStatus status) {
        boolean valido = status != null;
        if (!valido) {
            log.warn("   ⚠️ Status é nulo!");
        }
        return valido;
    }
}