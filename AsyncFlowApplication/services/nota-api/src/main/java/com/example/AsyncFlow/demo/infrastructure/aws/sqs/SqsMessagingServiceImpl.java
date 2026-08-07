package com.example.AsyncFlow.demo.infrastructure.aws.sqs;

import com.example.AsyncFlow.demo.application.Ports.MessagingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Log4j2
@Service
public class SqsMessagingServiceImpl implements MessagingService {

    private SqsClient client;
    private String queueUrl = "http://localhost:4566/000000000000/minha-fila";

    public SqsMessagingServiceImpl(SqsClient client) {
        this.client = client;
    }

    @Async
    @Override
    public void sendMenssage(String payload) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payload)
                .delaySeconds(5)
                .build();

        client.sendMessage(sendMessageRequest);
        log.info("Mensageria ativada " + payload);
    }
}
