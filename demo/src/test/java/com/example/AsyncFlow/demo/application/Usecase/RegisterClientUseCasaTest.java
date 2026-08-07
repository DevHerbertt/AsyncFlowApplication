package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.application.Ports.DTO.ClientRegisterDto;
import com.example.AsyncFlow.demo.application.Ports.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterClientUseCasaTest {


    @Mock
    ClientRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    RegisterClientUseCasa registerClientUseCasa;


    @Test
    void Deve_Retornar_Usuario_ja_existente(){

        ClientRegisterDto dto = new ClientRegisterDto();
        dto.setCpf("1234");


        when(repository.findbyid("1234"))
                .thenReturn(Optional.of(new Client()));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> registerClientUseCasa.register(dto)
        );


        assertEquals(
                "Usuario ja existente",
                exception.getMessage()
        );


        verify(repository, never()).save(any());
    }


    @Test
    void Deve_Cadastrar_Novo_Usuario(){


        ClientRegisterDto dto = new ClientRegisterDto();

        dto.setCpf("1234");
        dto.setName("Joao");
        dto.setEmail("joao@email.com");
        dto.setPassword("123456");
        dto.setCep("01000");


        when(repository.findbyid("1234"))
                .thenReturn(Optional.empty());


        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaCriptografada");


        registerClientUseCasa.register(dto);



        verify(repository).save(any(Client.class));

    }


    @Test
    void Deve_Criptografar_Senha_Antes_De_Salvar(){


        ClientRegisterDto dto = new ClientRegisterDto();

        dto.setCpf("1234");
        dto.setPassword("123456");


        when(repository.findbyid("1234"))
                .thenReturn(Optional.empty());


        when(passwordEncoder.encode("123456"))
                .thenReturn("HASH");


        registerClientUseCasa.register(dto);



        verify(passwordEncoder)
                .encode("123456");

    }



    @Test
    void Deve_Salvar_Com_Dados_Corretos(){


        ClientRegisterDto dto = new ClientRegisterDto();

        dto.setCpf("1234");
        dto.setName("Maria");
        dto.setEmail("maria@email.com");
        dto.setCep("99999");
        dto.setPassword("abc");


        when(repository.findbyid("1234"))
                .thenReturn(Optional.empty());


        when(passwordEncoder.encode("abc"))
                .thenReturn("HASH");


        ArgumentCaptor<Client> captor =
                ArgumentCaptor.forClass(Client.class);



        registerClientUseCasa.register(dto);



        verify(repository)
                .save(captor.capture());


        Client clientSalvo = captor.getValue();


        assertEquals("Maria", clientSalvo.getName());
        assertEquals("1234", clientSalvo.getCpf());
        assertEquals("maria@email.com", clientSalvo.getEmail());
        assertEquals("HASH", clientSalvo.getPassword());

    }



}