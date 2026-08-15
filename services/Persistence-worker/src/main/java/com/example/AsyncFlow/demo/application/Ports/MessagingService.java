package com.example.AsyncFlow.demo.application.Ports;

/**
 * Porta de saída (Output Port) para envio de mensagens para filas SQS.
 */
public interface MessagingService {
    void sendMessage(String payload);
}
