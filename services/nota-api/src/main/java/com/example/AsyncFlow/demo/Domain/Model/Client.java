package com.example.AsyncFlow.demo.Domain.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;


@Data
public class Client {

    private String email;
    private String cpf;
    private String name;
    private String cep;
    private String password;
    private List<NotaFiscal> notaFiscal;


}
