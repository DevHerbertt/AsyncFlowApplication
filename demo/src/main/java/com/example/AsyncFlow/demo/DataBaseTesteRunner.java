package com.example.AsyncFlow.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataBaseTesteRunner implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- TESTANDO BANCO DE DADOS ---");
        List<Map<String, Object>> clientes = jdbcTemplate.queryForList("SELECT * FROM client");
        clientes.forEach(System.out::println);
        System.out.println("--- FIM DO TESTE ---");
    }
}
