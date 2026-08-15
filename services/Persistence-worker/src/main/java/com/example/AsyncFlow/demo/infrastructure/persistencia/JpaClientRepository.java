package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade ClientEntity.
 */
public interface JpaClientRepository extends JpaRepository<ClientEntity, String> {
}
