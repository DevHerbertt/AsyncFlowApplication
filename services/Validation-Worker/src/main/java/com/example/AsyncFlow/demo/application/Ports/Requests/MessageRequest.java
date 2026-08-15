package com.example.AsyncFlow.demo.application.Ports.Requests;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import lombok.Data;

@Data
public class MessageRequest {
    private NotaFiscal notaFiscal;
}
