package com.example.AsyncFlow.demo.application.Ports.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;


@Data
public class ClientRegisterDto {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    private String cep;

    private String cpf;
    private String password;

    @Email
    private String email;

}
