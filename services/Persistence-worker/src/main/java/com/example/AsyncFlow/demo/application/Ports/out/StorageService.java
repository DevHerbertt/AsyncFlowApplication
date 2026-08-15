package com.example.AsyncFlow.demo.application.Ports.out;

/**
 * Porta de saída (Output Port) para armazenamento de arquivos no S3.
 */
public interface StorageService {

    /**
     * Faz upload de um arquivo para o S3.
     *
     * @param fileName    nome do arquivo (ex: nf-12345.xml)
     * @param fileBase64  conteúdo do arquivo codificado em Base64
     * @return URL pública/interna do arquivo no S3
     */
    String uploadFile(String fileName, String fileBase64);
}
