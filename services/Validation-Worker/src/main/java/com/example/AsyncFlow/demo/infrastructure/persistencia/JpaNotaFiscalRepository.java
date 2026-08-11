package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotaFiscalRepository extends JpaRepository<NotaFiscalEntity,String> {
}
