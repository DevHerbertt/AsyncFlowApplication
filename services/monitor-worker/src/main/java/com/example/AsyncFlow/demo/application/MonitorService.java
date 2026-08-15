package com.example.AsyncFlow.demo.application;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.infrastructure.aws.s3.S3MonitorService;
import com.example.AsyncFlow.demo.infrastructure.aws.sqs.SqsMonitorService;
import com.example.AsyncFlow.demo.infrastructure.persistencia.JpaClientMonitorRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.JpaItemMonitorRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.JpaNotaFiscalMonitorRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ClientMonitorEntity;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ItemMonitorEntity;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalMonitorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de aplicação do monitor-worker.
 * Agrega dados do PostgreSQL (notas, clientes, itens), filas SQS e arquivos S3.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final JpaNotaFiscalMonitorRepository notaFiscalRepository;
    private final JpaClientMonitorRepository clientRepository;
    private final JpaItemMonitorRepository itemRepository;
    private final SqsMonitorService sqsMonitorService;
    private final S3MonitorService s3MonitorService;

    public List<NotaFiscalMonitorEntity> getAllNotas() {
        return notaFiscalRepository.findAllByOrderByBuyDateDesc();
    }

    public List<NotaFiscalMonitorEntity> getNotasByStatus(NotaFiscalStatus status) {
        return notaFiscalRepository.findByStatus(status);
    }

    public Map<String, Long> getContagemPorStatus() {
        Map<String, Long> contagem = new LinkedHashMap<>();
        for (NotaFiscalStatus s : NotaFiscalStatus.values()) {
            contagem.put(s.name(), 0L);
        }
        List<Object[]> resultado = notaFiscalRepository.countByStatus();
        for (Object[] row : resultado) {
            NotaFiscalStatus status = (NotaFiscalStatus) row[0];
            Long count = (Long) row[1];
            contagem.put(status.name(), count);
        }
        return contagem;
    }

    public List<ClientMonitorEntity> getAllClientes() {
        return clientRepository.findAll();
    }

    public List<ItemMonitorEntity> getAllItens() {
        return itemRepository.findAll();
    }

    public Map<String, SqsMonitorService.QueueInfo> getQueuesInfo() {
        return sqsMonitorService.getQueuesInfo();
    }

    public List<S3MonitorService.S3FileInfo> getS3Files() {
        return s3MonitorService.listFiles();
    }

    public String getS3BucketName() {
        return s3MonitorService.getBucketName();
    }

    public DashboardData getDashboardData() {
        return new DashboardData(
                getAllNotas(),
                getContagemPorStatus(),
                getQueuesInfo(),
                getAllClientes(),
                getAllItens(),
                getS3Files(),
                getS3BucketName()
        );
    }

    public record DashboardData(
            List<NotaFiscalMonitorEntity> notas,
            Map<String, Long> contagemPorStatus,
            Map<String, SqsMonitorService.QueueInfo> filas,
            List<ClientMonitorEntity> clientes,
            List<ItemMonitorEntity> itens,
            List<S3MonitorService.S3FileInfo> arquivosS3,
            String bucketName
    ) {}
}
