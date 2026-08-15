package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade ItemEntity.
 */
public interface JpaItemRepository extends JpaRepository<ItemEntity, String> {
}
