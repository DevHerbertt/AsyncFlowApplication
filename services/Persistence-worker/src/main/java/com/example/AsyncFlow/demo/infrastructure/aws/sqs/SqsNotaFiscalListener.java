package com.example.AsyncFlow.demo.infrastructure.aws.sqs;

import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.Requests.MessageRequest;
import com.example.AsyncFlow.demo.application.Ports.in.PersistenceNotaFiscalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Listener SQS que consome mensagens da fila fila.notas.processadas.
 * Delega o processamento ao caso de uso PersistenceNotaFiscalService.
 *
 * Segue o princípio de Inversão de Dependência (DIP):
 * depende da abstração (interface), não da implementação concreta.
 */
@Log4j2
@Component
public class SqsNotaFiscalListener {

    private final PersistenceNotaFiscalService persistenceNotaFiscalService;
    private final ObjectMapper objectMapper;

    public SqsNotaFiscalListener(
            PersistenceNotaFiscalService persistenceNotaFiscalService,
            ObjectMapper objectMapper) {
        this.persistenceNotaFiscalService = persistenceNotaFiscalService;
        this.objectMapper = objectMapper;
    }

    /**
     * Consome mensagens da fila fila.notas.processadas.
     * Cada mensagem contém uma NotaFiscal validada pelo validation-worker.
     *
     * @param payload JSON da mensagem recebida
     */
    @SqsListener("${persistence.sqs.input-queue-name}")
    public void onMessage(String payload) {
        log.info("Mensagem recebida da fila fila.notas.processadas: {}", payload);
        try {
            MessageRequest messageRequest = objectMapper.readValue(payload, MessageRequest.class);
            NotaFiscal notaFiscal = messageRequest.getNotaFiscal();

            if (notaFiscal == null) {
                log.error("Mensagem inválida: notaFiscal é nula. Payload: {}", payload);
                return;
            }

            persistenceNotaFiscalService.persist(notaFiscal);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila: {}. Erro: {}", payload, e.getMessage(), e);
            throw new RuntimeException("Falha ao processar mensagem da fila fila.notas.processadas", e);
        }
    }
}
