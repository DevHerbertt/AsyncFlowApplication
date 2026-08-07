package com.example.AsyncFlow.demo.infrastructure.persistencia;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.application.Ports.ClientRepository;

import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    private JpaClientRepository jpaClientRepository;

    @Override
    public Optional<Client> findbyid(long cpf) {
        Optional<Client> client = jpaClientRepository.findById(cpf);
        return client;
    }

    @Override
    public Optional<Client> findbyid(String cpf) {
        return Optional.empty();
    }

    @Override
    public void save(Client client) {
        jpaClientRepository.save(client);

    }
}
