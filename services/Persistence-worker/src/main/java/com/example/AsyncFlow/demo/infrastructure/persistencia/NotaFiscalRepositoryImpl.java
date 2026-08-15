package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.Domain.Model.Item;
import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import com.example.AsyncFlow.demo.application.Ports.out.NotaFiscalRepository;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ClientEntity;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ItemEntity;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da porta de saída NotaFiscalRepository.
 * Responsável por persistir clientes, notas fiscais e itens no PostgreSQL.
 */
@Log4j2
@Repository
public class NotaFiscalRepositoryImpl implements NotaFiscalRepository {

    private final JpaNotaFiscalRepository jpaNotaFiscalRepository;
    private final JpaClientRepository jpaClientRepository;
    private final JpaItemRepository jpaItemRepository;

    public NotaFiscalRepositoryImpl(
            JpaNotaFiscalRepository jpaNotaFiscalRepository,
            JpaClientRepository jpaClientRepository,
            JpaItemRepository jpaItemRepository) {
        this.jpaNotaFiscalRepository = jpaNotaFiscalRepository;
        this.jpaClientRepository = jpaClientRepository;
        this.jpaItemRepository = jpaItemRepository;
    }

    @Override
    public NotaFiscalStatus save(NotaFiscal notaFiscal, String s3Url) {
        // 1. Persiste ou atualiza o cliente
        ClientEntity clientEntity = resolveClient(notaFiscal.getClient());

        // 2. Monta a entidade da nota fiscal
        NotaFiscalEntity notaFiscalEntity = buildNotaFiscalEntity(notaFiscal, clientEntity, s3Url);

        // 3. Salva a nota fiscal (cascade salva os itens)
        jpaNotaFiscalRepository.save(notaFiscalEntity);
        log.info("Nota fiscal {} persistida com status {} e s3Url {}",
                notaFiscal.getNumeroNota(), notaFiscal.getStatus(), s3Url);

        return notaFiscal.getStatus();
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares privados (Single Responsibility)
    // -------------------------------------------------------------------------

    private ClientEntity resolveClient(Client client) {
        if (client == null) {
            return null;
        }
        return jpaClientRepository.findById(client.cpf())
                .orElseGet(() -> {
                    ClientEntity newClient = new ClientEntity();
                    newClient.setCpf(client.cpf());
                    newClient.setName(client.name());
                    log.info("Novo cliente criado: {}", client.cpf());
                    return jpaClientRepository.save(newClient);
                });
    }

    private NotaFiscalEntity buildNotaFiscalEntity(NotaFiscal notaFiscal, ClientEntity clientEntity, String s3Url) {
        NotaFiscalEntity entity = jpaNotaFiscalRepository.findById(notaFiscal.getNumeroNota())
                .orElse(new NotaFiscalEntity());

        entity.setNumeroNota(notaFiscal.getNumeroNota());
        entity.setClient(clientEntity);
        entity.setTotalValue(notaFiscal.getTotalValue());
        entity.setBuyDate(notaFiscal.getBuyDate());
        entity.setCnpj(notaFiscal.getCnpj());
        entity.setStatus(notaFiscal.getStatus());
        entity.setS3Url(s3Url);

        // Monta os itens
        List<ItemEntity> itemEntities = buildItemEntities(notaFiscal.getItens(), entity);
        entity.setItens(itemEntities);

        return entity;
    }

    private List<ItemEntity> buildItemEntities(List<Item> itens, NotaFiscalEntity notaFiscalEntity) {
        if (itens == null || itens.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemEntity> entities = new ArrayList<>();
        for (Item item : itens) {
            ItemEntity itemEntity = jpaItemRepository.findById(item.getSerieNumber())
                    .orElse(new ItemEntity());
            itemEntity.setSerieNumber(item.getSerieNumber());
            itemEntity.setName(item.getName());
            itemEntity.setPrice(item.getPrice());
            itemEntity.setNotaFiscal(notaFiscalEntity);
            entities.add(itemEntity);
        }
        return entities;
    }
}
