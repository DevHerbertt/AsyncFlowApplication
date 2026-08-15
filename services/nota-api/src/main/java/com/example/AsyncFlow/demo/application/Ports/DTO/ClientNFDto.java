package com.example.AsyncFlow.demo.application.Ports.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;


@Data
public class ClientNFDto {

    @NotBlank
    private String name;

    private String cpf;




}
