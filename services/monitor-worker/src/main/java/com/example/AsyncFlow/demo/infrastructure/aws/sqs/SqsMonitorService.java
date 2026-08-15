package com.example.AsyncFlow.demo.infrastructure.aws.sqs;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Serviço que consulta os atributos das filas SQS no LocalStack
 * para exibir no dashboard de monitoramento.
 */
@Log4j2
@Service
public class SqsMonitorService {

    private final SqsClient sqsClient;
    private final String localstackEndpoint;

    // Nomes das filas monitoradas
    private static final String[] QUEUE_NAMES = {
            "validacao-vendas",
            "notas-processadas",
            "fila-monitor"
    };

    public SqsMonitorService(
            SqsClient sqsClient,
            @Value("${monitor.sqs.endpoint:http://localhost:4566}") String localstackEndpoint) {
        this.sqsClient = sqsClient;
        this.localstackEndpoint = localstackEndpoint;
    }

    /**
     * Retorna um mapa com o nome de cada fila e a quantidade de mensagens visíveis.
     */
    public Map<String, QueueInfo> getQueuesInfo() {
        Map<String, QueueInfo> result = new HashMap<>();

        for (String queueName : QUEUE_NAMES) {
            try {
                String queueUrl = buildQueueUrl(queueName);
                GetQueueAttributesResponse attrs = sqsClient.getQueueAttributes(
                        GetQueueAttributesRequest.builder()
                                .queueUrl(queueUrl)
                                .attributeNames(
                                        QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES,
                                        QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE,
                                        QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_DELAYED
                                )
                                .build()
                );

                int visible = parseInt(attrs.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
                int inFlight = parseInt(attrs.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE));
                int delayed = parseInt(attrs.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES_DELAYED));

                result.put(queueName, new QueueInfo(queueName, visible, inFlight, delayed, "OK"));

            } catch (Exception e) {
                log.warn("Fila {} nao encontrada ou erro ao consultar: {}", queueName, e.getMessage());
                result.put(queueName, new QueueInfo(queueName, 0, 0, 0, "INDISPONIVEL"));
            }
        }

        return result;
    }

    private String buildQueueUrl(String queueName) {
        return localstackEndpoint + "/000000000000/" + queueName;
    }

    private int parseInt(String value) {
        if (value == null) return 0;
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { return 0; }
    }

    // ─── DTO interno ────────────────────────────────────────────────────────────
    public record QueueInfo(
            String nome,
            int mensagensVisiveis,
            int mensagensEmProcessamento,
            int mensagensAtrasadas,
            String status
    ) {}
}
