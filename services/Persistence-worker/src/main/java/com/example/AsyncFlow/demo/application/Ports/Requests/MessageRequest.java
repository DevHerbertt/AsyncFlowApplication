package com.example.AsyncFlow.demo.application.Ports.Requests;

import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import lombok.Data;

/**
 * Objeto que representa a mensagem recebida da fila fila.notas.processadas.
 */
@Data
public class MessageRequest {
    private NotaFiscal notaFiscal;
}
