package com.example.AsyncFlow.demo.application.service;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.Ports.MessagingService;
import com.example.AsyncFlow.demo.application.Ports.Requests.MessageRequest;
import com.example.AsyncFlow.demo.application.Usecase.StatusNotaFiscalUsercase;
import com.example.AsyncFlow.demo.application.Usecase.ValidationNotafiscalUsercase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço de aplicação que orquestra a validação e atualização de status
 * de uma nota fiscal recebida da fila.
 *
 * Fluxo:
 *  1. Valida a nota fiscal (CNPJ, itens, datas, etc.)
 *  2. Salva o status (VALIDADA ou INVALIDA) no banco
 *  3. Publica evento na fila de auditoria
 */
@Service
public class ProcessarNotaFiscalService {

    private static final Logger log = LoggerFactory.getLogger(ProcessarNotaFiscalService.class);

    private final ValidationNotafiscalUsercase validationNotafiscalUsercase;
    private final StatusNotaFiscalUsercase statusNotaFiscalUsercase;
    private final MessagingService messagingService;

    public ProcessarNotaFiscalService(
            ValidationNotafiscalUsercase validationNotafiscalUsercase,
            StatusNotaFiscalUsercase statusNotaFiscalUsercase,
            MessagingService messagingService) {
        this.validationNotafiscalUsercase = validationNotafiscalUsercase;
        this.statusNotaFiscalUsercase = statusNotaFiscalUsercase;
        this.messagingService = messagingService;
    }

    public void processar(MessageRequest messageRequest) {
        String numeroNota = messageRequest.getNotaFiscal() != null
                ? messageRequest.getNotaFiscal().getNumeroNota()
                : "NULO";

        log.info("Processamento iniciado para nota: {}", numeroNota);

        try {
            boolean valida = validationNotafiscalUsercase.validation(messageRequest.getNotaFiscal());
            log.info("Resultado da validacao: {}", valida ? "VALIDA" : "INVALIDA");

            NotaFiscalStatus status = valida ? NotaFiscalStatus.VALIDADA : NotaFiscalStatus.INVALIDA;

            // Salva o status no banco (VALIDADA ou INVALIDA)
            statusNotaFiscalUsercase.execute(messageRequest, status);
            log.info("Status {} salvo no banco para nota {}", status, numeroNota);

        } catch (Exception e) {
            log.error("Excecao durante processamento da nota {}: {}", numeroNota, e.getMessage(), e);
            try {
                statusNotaFiscalUsercase.execute(messageRequest, NotaFiscalStatus.ERRO);
                log.info("Status ERRO salvo para nota {}", numeroNota);
            } catch (Exception ex) {
                log.error("Falha ao salvar status ERRO: {}", ex.getMessage(), ex);
            }
            throw new RuntimeException("Erro ao processar nota fiscal " + numeroNota, e);
        }

        // Publica evento na fila de auditoria
        log.info("Enviando mensagem para fila auditoria-vendas...");
        messagingService.sendMessage(messageRequest.toString());
        log.info("Processamento finalizado para nota: {}", numeroNota);
    }
}
