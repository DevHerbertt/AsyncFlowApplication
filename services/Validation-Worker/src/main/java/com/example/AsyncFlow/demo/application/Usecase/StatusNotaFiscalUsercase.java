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

    /**
     * Salva o status da nota fiscal no banco.
     * Sobrecarga sem status: usa VALIDADA como padrão (compatibilidade).
     */
    public NotaFiscalStatus execute(MessageRequest messageRequest) {
        return execute(messageRequest, NotaFiscalStatus.VALIDADA);
    }

    /**
     * Salva o status informado para a nota fiscal no banco.
     *
     * @param messageRequest mensagem com a nota fiscal
     * @param status         status a ser salvo (VALIDADA, INVALIDA, ERRO, etc.)
     */
    public NotaFiscalStatus execute(MessageRequest messageRequest, NotaFiscalStatus status) {
        System.out.println("execute NFUseCase iniciado - status: " + status);

        String numeroNota = messageRequest.getNotaFiscal().getNumeroNota();

        System.out.println("execute NFUseCase finalizado - nota: " + numeroNota);
        return repository.saveStatus(numeroNota, status);
    }
}
