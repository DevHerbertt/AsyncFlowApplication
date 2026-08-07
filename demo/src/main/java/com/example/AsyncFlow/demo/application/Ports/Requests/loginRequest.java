package com.example.AsyncFlow.demo.application.Ports.Requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;


@Data
public class loginRequest {

    @CPF(message = "CPF invalido")
    private String cpf;

    @NotBlank
    private String password;
}
