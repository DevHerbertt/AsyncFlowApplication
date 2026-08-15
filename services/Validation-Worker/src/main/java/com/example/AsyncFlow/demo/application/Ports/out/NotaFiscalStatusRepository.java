package com.example.AsyncFlow.demo.application.Ports.out;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;

public interface NotaFiscalStatusRepository {
    NotaFiscalStatus saveStatus(String numeroNota, NotaFiscalStatus status);
}
