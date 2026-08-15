package com.example.AsyncFlow.demo.infrastructure.aws.sqs;

import com.example.AsyncFlow.demo.application.Ports.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SqsMessagingServiceImpl implements MessagingService {

    private static final Logger log = LoggerFactory.getLogger(SqsMessagingServiceImpl.class);

    private final SqsClient client;
    private final String queueUrl = "http://localhost:4566/000000000000/auditoria-vendas";

    public SqsMessagingServiceImpl(SqsClient client) {
        this.client = client;
    }

    @Async
    @Override
    public void sendMessage(String payload) {
        log.info("📤 SqsMessagingServiceImpl.sendMessage() INICIADO");
        log.info("   Payload: {}", payload);
        log.info("   Fila destino: auditoria-vendas");
        log.info("   QueueUrl: {}", queueUrl);
        log.info("   Delay: 5 segundos");

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payload)
                .delaySeconds(5)
                .build();

        try {
            SendMessageResponse response = client.sendMessage(sendMessageRequest);
            log.info("✅ Mensagem enviada para auditoria-vendas com sucesso!");
            log.info("   MessageId: {}", response.messageId());
        } catch (Exception e) {
            log.error("❌ Erro ao enviar mensagem para auditoria-vendas: {}", e.getMessage(), e);
            throw e;
        }

        log.info("🏁 SqsMessagingServiceImpl.sendMessage() FINALIZADO");
    }
}