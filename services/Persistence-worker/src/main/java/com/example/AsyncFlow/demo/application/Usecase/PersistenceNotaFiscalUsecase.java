package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.MessagingService;
import com.example.AsyncFlow.demo.application.Ports.in.PersistenceNotaFiscalService;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalRepository;
import com.example.AsyncFlow.demo.application.Ports.out.StorageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Caso de uso principal do Persistence-Worker.
 *
 * Responsabilidades (Single Responsibility / Clean Architecture):
 *  1. Faz upload do arquivo XML/PDF para o S3 (se existir)
 *  2. Persiste a nota fiscal, cliente e itens no PostgreSQL com status SALVA
 *  3. Atualiza o status para CONCLUIDA
 *  4. Publica evento na fila.monitor
 */
@Log4j2
@Service
public class PersistenceNotaFiscalUsecase implements PersistenceNotaFiscalService {

    private final NotaFiscalRepository notaFiscalRepository;
    private final StorageService storageService;
    private final MessagingService messagingService;

    public PersistenceNotaFiscalUsecase(
            NotaFiscalRepository notaFiscalRepository,
            StorageService storageService,
            MessagingService messagingService) {
        this.notaFiscalRepository = notaFiscalRepository;
        this.storageService = storageService;
        this.messagingService = messagingService;
    }

    @Override
    public void persist(NotaFiscal notaFiscal) {
        log.info("Iniciando persistência da nota fiscal: {}", notaFiscal.getNumeroNota());

        // 1. Upload do arquivo para o S3 (se houver)
        String s3Url = null;
        if (notaFiscal.getFileBase64() != null && !notaFiscal.getFileBase64().isBlank()) {
            log.info("Arquivo detectado. Fazendo upload para o S3: {}", notaFiscal.getFileName());
            s3Url = storageService.uploadFile(notaFiscal.getFileName(), notaFiscal.getFileBase64());
            log.info("Upload concluído. S3 URL: {}", s3Url);
        }

        // 2. Persiste no PostgreSQL com status SALVA e a URL do S3
        notaFiscal.setStatus(NotaFiscalStatus.SALVA);
        NotaFiscalStatus statusSalva = notaFiscalRepository.save(notaFiscal, s3Url);
        log.info("Nota fiscal {} salva no banco com status: {}", notaFiscal.getNumeroNota(), statusSalva);

        // 3. Atualiza para CONCLUIDA
        notaFiscal.setStatus(NotaFiscalStatus.CONCLUIDA);
        NotaFiscalStatus statusFinal = notaFiscalRepository.save(notaFiscal, s3Url);
        log.info("Nota fiscal {} atualizada para status: {}", notaFiscal.getNumeroNota(), statusFinal);

        // 4. Publica evento na fila.monitor
        String evento = buildMonitorEvent(notaFiscal, s3Url, statusFinal);
        messagingService.sendMessage(evento);
        log.info("Evento publicado na fila.monitor para nota: {}", notaFiscal.getNumeroNota());
    }

    private String buildMonitorEvent(NotaFiscal notaFiscal, String s3Url, NotaFiscalStatus status) {
        return String.format(
                "{\"numeroNota\":\"%s\",\"status\":\"%s\",\"s3Url\":\"%s\"}",
                notaFiscal.getNumeroNota(),
                status,
                s3Url != null ? s3Url : ""
        );
    }
}
