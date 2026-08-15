package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalStatusRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Log4j2
@Repository
public class NotaFiscalStatusRespositoryImpl implements NotaFiscalStatusRepository {

    @Autowired
    private JpaNotaFiscalRepository repository;


@Override
    public NotaFiscalStatus saveStatus(String numeroNota, NotaFiscalStatus status) {

        Optional<NotaFiscalEntity> notaRepository = repository.findById(numeroNota);

        if (notaRepository.isPresent()) {
            log.info("Nota Fiscal encontrada");

            NotaFiscalEntity notaFiscal = notaRepository.get();

            notaFiscal.setStatus(status);

            repository.save(notaFiscal);

            return status;
        } else {
            log.error("Nota fiscal nao encontrada");
            throw new RuntimeException("Nota fiscal nao encontrada para o numero: " + numeroNota);
        }
    }
}
