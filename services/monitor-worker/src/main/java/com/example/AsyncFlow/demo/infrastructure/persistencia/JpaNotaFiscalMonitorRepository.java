package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Repositório JPA somente-leitura para o dashboard de monitoramento.
 */
public interface JpaNotaFiscalMonitorRepository extends JpaRepository<NotaFiscalMonitorEntity, String> {

    List<NotaFiscalMonitorEntity> findByStatus(NotaFiscalStatus status);

    List<NotaFiscalMonitorEntity> findAllByOrderByBuyDateDesc();

    @Query("SELECT n.status, COUNT(n) FROM NotaFiscalMonitorEntity n GROUP BY n.status")
    List<Object[]> countByStatus();
}
