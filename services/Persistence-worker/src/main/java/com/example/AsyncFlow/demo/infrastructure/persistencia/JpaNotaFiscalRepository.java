package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade NotaFiscalEntity.
 */
public interface JpaNotaFiscalRepository extends JpaRepository<NotaFiscalEntity, String> {
}
