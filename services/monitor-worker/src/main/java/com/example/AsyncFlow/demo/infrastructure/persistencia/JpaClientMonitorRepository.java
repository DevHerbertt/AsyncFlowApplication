package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ClientMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClientMonitorRepository extends JpaRepository<ClientMonitorEntity, String> {
}
