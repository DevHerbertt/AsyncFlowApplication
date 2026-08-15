package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ItemMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaItemMonitorRepository extends JpaRepository<ItemMonitorEntity, String> {
    List<ItemMonitorEntity> findByNotaFiscalNumeroNota(String numeroNota);
}
