package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.Ports.Requests.MessageRequest;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusNotaFiscalUsercase {

    private final NotaFiscalStatusRepository repository;

    public StatusNotaFiscalUsercase(NotaFiscalStatusRepository repository) {
        this.repository = repository;
    }

    public  NotaFiscalStatus execute(MessageRequest messageRequest){
        System.out.println("execute NFUseCase iniciado");

        String numeroNota = messageRequest.getNotaFiscal().getNumeroNota();

        NotaFiscalStatus status = NotaFiscalStatus.VALIDADA;

        System.out.println("execute NFUseCase finalizado");
        return repository.saveStatus(numeroNota,status);
    }
}
