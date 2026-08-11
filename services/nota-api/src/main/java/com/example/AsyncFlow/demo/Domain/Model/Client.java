package com.example.AsyncFlow.demo.Domain.Model;

import com.example.AsyncFlow.demo.application.Ports.Requests.NotaFiscalRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String cep;
    private String password;
    private List<NotaFiscalRequest> requests;
}
