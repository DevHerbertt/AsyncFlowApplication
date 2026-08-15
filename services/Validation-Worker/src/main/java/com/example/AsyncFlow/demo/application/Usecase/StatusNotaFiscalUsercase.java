package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.Ports.Requests.MessageRequest;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StatusNotaFiscalUsercase {

    private static final Logger log = LoggerFactory.getLogger(StatusNotaFiscalUsercase.class);

    private final NotaFiscalStatusRepository repository;
    private final ValidationNotafiscalUsercase validationNotafiscalUsercase;

    public StatusNotaFiscalUsercase(NotaFiscalStatusRepository repository, ValidationNotafiscalUsercase validationNotafiscalUsercase) {
        this.repository = repository;
        this.validationNotafiscalUsercase = validationNotafiscalUsercase;
    }

    public NotaFiscalStatus execute(MessageRequest messageRequest){
        log.info("📝 StatusNotaFiscalUsercase.execute(sem status) INICIADO");
        String numeroNota = messageRequest.getNotaFiscal() != null ? messageRequest.getNotaFiscal().getNumeroNota() : "NULO";
        log.info("   Número da nota: {}", numeroNota);

        // Validar antes de salvar
        if (!validationNotafiscalUsercase.validation(messageRequest.getNotaFiscal())) {
            log.warn("   ⚠️ Nota fiscal {} INVÁLIDA, lançando exceção", numeroNota);
            throw new RuntimeException("Nota fiscal inválida: " + numeroNota);
        }

        NotaFiscalStatus status = NotaFiscalStatus.VALIDADA;
        log.info("   Salvando nota {} com status: {}", numeroNota, status);

        NotaFiscalStatus resultado = repository.saveStatus(numeroNota, status);
        log.info("✅ Status {} salvo com sucesso para nota {}", resultado, numeroNota);
        return resultado;
    }

    public NotaFiscalStatus execute(MessageRequest messageRequest, NotaFiscalStatus status){
        log.info("📝 StatusNotaFiscalUsercase.execute(status={}) INICIADO", status);
        String numeroNota = messageRequest.getNotaFiscal() != null ? messageRequest.getNotaFiscal().getNumeroNota() : "NULO";
        log.info("   Número da nota: {}", numeroNota);

        // Validar antes de salvar
        if (!validationNotafiscalUsercase.validation(messageRequest.getNotaFiscal())) {
            log.warn("   ⚠️ Nota fiscal {} INVÁLIDA (dados inválidos), alterando status para INVALIDA", numeroNota);
            status = NotaFiscalStatus.INVALIDA;
        }

        log.info("   Salvando nota {} com status: {}", numeroNota, status);

        NotaFiscalStatus resultado = repository.saveStatus(numeroNota, status);
        log.info("✅ Status {} salvo com sucesso para nota {}", resultado, numeroNota);
        return resultado;
    }
}