package com.example.AsyncFlow.demo.application.Ports.Requests;

import com.example.AsyncFlow.demo.Domain.Model.NotaFiscal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

public class RegisterRequest {
    @Email(message = "Email invalido")
    private String email;

    @CPF
    private String cpf;
    private String name;
    private String cep;
    private String password;

}
