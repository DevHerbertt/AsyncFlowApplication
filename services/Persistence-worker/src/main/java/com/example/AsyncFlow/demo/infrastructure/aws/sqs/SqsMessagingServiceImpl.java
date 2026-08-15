package com.example.AsyncFlow.demo.infrastructure.aws.sqs;

import com.example.AsyncFlow.demo.application.Ports.MessagingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/**
 * Implementação da porta de saída MessagingService.
 * Envia mensagens para a fila fila.monitor no LocalStack SQS.
 */
@Log4j2
@Service
public class SqsMessagingServiceImpl implements MessagingService {

    private final SqsClient sqsClient;
    private final String monitorQueueUrl;

    public SqsMessagingServiceImpl(
            SqsClient sqsClient,
            @Value("${persistence.sqs.monitor-queue-url}") String monitorQueueUrl) {
        this.sqsClient = sqsClient;
        this.monitorQueueUrl = monitorQueueUrl;
    }

    @Async
    @Override
    public void sendMessage(String payload) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(monitorQueueUrl)
                .messageBody(payload)
                .delaySeconds(0)
                .build();

        sqsClient.sendMessage(request);
        log.info("Evento publicado na fila.monitor: {}", payload);
    }
}
