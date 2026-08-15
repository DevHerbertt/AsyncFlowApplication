package com.example.AsyncFlow.demo.application;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.infrastructure.aws.sqs.SqsMonitorService;
import com.example.AsyncFlow.demo.infrastructure.persistencia.JpaNotaFiscalMonitorRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalMonitorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de aplicação do monitor-worker.
 * Agrega dados do PostgreSQL e das filas SQS para o dashboard.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final JpaNotaFiscalMonitorRepository notaFiscalRepository;
    private final SqsMonitorService sqsMonitorService;

    /**
     * Retorna todas as notas fiscais ordenadas por data (mais recentes primeiro).
     */
    public List<NotaFiscalMonitorEntity> getAllNotas() {
        return notaFiscalRepository.findAllByOrderByBuyDateDesc();
    }

    /**
     * Retorna notas filtradas por status.
     */
    public List<NotaFiscalMonitorEntity> getNotasByStatus(NotaFiscalStatus status) {
        return notaFiscalRepository.findByStatus(status);
    }

    /**
     * Retorna contagem de notas por status.
     */
    public Map<String, Long> getContagemPorStatus() {
        Map<String, Long> contagem = new LinkedHashMap<>();
        // Inicializa todos os status com 0
        for (NotaFiscalStatus s : NotaFiscalStatus.values()) {
            contagem.put(s.name(), 0L);
        }
        // Preenche com os valores reais do banco
        List<Object[]> resultado = notaFiscalRepository.countByStatus();
        for (Object[] row : resultado) {
            NotaFiscalStatus status = (NotaFiscalStatus) row[0];
            Long count = (Long) row[1];
            contagem.put(status.name(), count);
        }
        return contagem;
    }

    /**
     * Retorna informações das filas SQS.
     */
    public Map<String, SqsMonitorService.QueueInfo> getQueuesInfo() {
        return sqsMonitorService.getQueuesInfo();
    }

    /**
     * Agrega todos os dados para o dashboard.
     */
    public DashboardData getDashboardData() {
        return new DashboardData(
                getAllNotas(),
                getContagemPorStatus(),
                getQueuesInfo()
        );
    }

    public record DashboardData(
            List<NotaFiscalMonitorEntity> notas,
            Map<String, Long> contagemPorStatus,
            Map<String, SqsMonitorService.QueueInfo> filas
    ) {}
}
