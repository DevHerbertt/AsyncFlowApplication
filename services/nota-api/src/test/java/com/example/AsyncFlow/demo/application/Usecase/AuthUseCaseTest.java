package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.application.Ports.DTO.ClientLoginDto;
import com.example.AsyncFlow.demo.application.Ports.ClientRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@Log4j2
@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AuthUseCase authUseCase;





    @Test
    void Deve_logar_com_sucesso() {
        log.info("\n---Deve_logar_com_sucesso()----");

        Client clientFake = new Client();
        clientFake.setCpf("1234");
        clientFake.setPassword("SenhaDecodificada");

        ClientLoginDto clientDTo = new ClientLoginDto();
        clientDTo.setCpf("1234");
        clientDTo.setPassword("senhamock");


        when(clientRepository.findbyid("1234")
        ).thenReturn(Optional.of(clientFake));

        when(passwordEncoder.matches(clientDTo.getPassword(),
                clientFake.getPassword())).thenReturn(true);


        assertDoesNotThrow(() -> authUseCase.login(clientDTo));
    }

    @Test
    void Deve_retornar_erro_quando_senha_estiver_incorreta() {

        log.info("\n---Deve_retornar_erro_quando_senha_estiver_incorreta()----");


        Client clientFake = new Client();
        clientFake.setCpf("1234");
        clientFake.setPassword("senhamock");

        ClientLoginDto clientDTo = new ClientLoginDto();
        clientDTo.setCpf("1234");
        clientDTo.setPassword("senhamock");

        when(clientRepository.findbyid(clientDTo.getCpf())
        ).thenReturn(Optional.of(clientFake));

        when(passwordEncoder.matches(clientDTo.getPassword(),clientFake.getPassword())
        ).thenReturn(false);


       RuntimeException exception = assertThrows(RuntimeException.class,
               () -> authUseCase.login(clientDTo));
       log.info(exception.getMessage());

       assertEquals("Senha invalida",exception.getMessage());
    }
    @Test
    void Deve_retornar_erro_quando_cpf_nao_existir() {

        log.info("\n---Deve_retornar_erro_quando_cpf_estiver_incorreta()----");

        ClientLoginDto clientDTo = new ClientLoginDto();
        clientDTo.setCpf("1234");
        clientDTo.setPassword("senhamock");

        when(clientRepository.findbyid(clientDTo.getCpf())
        ).thenReturn(Optional.empty());


       RuntimeException exception = assertThrows(RuntimeException.class,
               () -> authUseCase.login(clientDTo));

       log.info(exception.getMessage());
       log.info("Senha não foi validada");

       assertEquals("CPF não Encontrado",exception.getMessage());
    }

}