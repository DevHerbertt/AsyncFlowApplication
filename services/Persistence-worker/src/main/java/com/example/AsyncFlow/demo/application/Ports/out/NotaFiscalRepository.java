package com.example.AsyncFlow.demo.application.Ports.out;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;

/**
 * Porta de saída (Output Port) para persistência da nota fiscal no banco de dados.
 */
public interface NotaFiscalRepository {

    /**
     * Salva ou atualiza a nota fiscal completa (cliente, itens, status, s3Url).
     *
     * @param notaFiscal a nota fiscal a ser persistida
     * @param s3Url      URL do arquivo no S3 (pode ser null se não houver arquivo)
     * @return o status atualizado
     */
    NotaFiscalStatus save(NotaFiscal notaFiscal, String s3Url);
}
