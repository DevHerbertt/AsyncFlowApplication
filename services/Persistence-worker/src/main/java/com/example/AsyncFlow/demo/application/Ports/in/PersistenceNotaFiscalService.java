package com.example.AsyncFlow.demo.application.Ports.in;

import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;

/**
 * Porta de entrada (Input Port) do Persistence-Worker.
 * Define o contrato para persistência de uma nota fiscal validada.
 */
public interface PersistenceNotaFiscalService {

    /**
     * Persiste a nota fiscal no banco de dados e, se houver arquivo,
     * faz o upload para o S3. Ao final, publica evento na fila.monitor.
     *
     * @param notaFiscal a nota fiscal validada recebida da fila
     */
    void persist(NotaFiscal notaFiscal);
}
