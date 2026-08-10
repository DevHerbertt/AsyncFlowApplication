package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.Ports.Requests.MessageRequest;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalStatusRepository;

public class StatusNotaFiscalUsercase implements NotaFiscalStatusRepository {

   private final NotaFiscalStatusRepository repository;

    public StatusNotaFiscalUsercase(NotaFiscalStatusRepository repository) {
        this.repository = repository;
    }

    @Override
   public NotaFiscalStatus saveStatus(String numeroNota, NotaFiscalStatus status) {
      return null;
   }
}
