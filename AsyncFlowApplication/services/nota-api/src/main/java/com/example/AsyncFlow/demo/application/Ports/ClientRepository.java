package com.example.AsyncFlow.demo.application.Ports;

import com.example.AsyncFlow.demo.Domain.Model.Client;

import java.util.Optional;

public interface ClientRepository {
    void findbyid(String cpf);
    void save(Client client);
}
