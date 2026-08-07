package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.application.Ports.DTO.ClientLoginDto;
import com.example.AsyncFlow.demo.application.Ports.ClientRepository;
import com.example.AsyncFlow.demo.application.Ports.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Log4j2
public class AuthUseCase implements LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private ClientRepository clientRepository;

    public AuthUseCase(PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public void login(ClientLoginDto clientLoginDto) {
        Optional<Client> client = clientRepository.findbyid(clientLoginDto.getCpf());

        if (client.isPresent()){

            Client clientInstace = client.get();
            log.info("CPF encontrado");

            if (passwordEncoder.matches(clientLoginDto.getPassword(),
                    clientInstace.getPassword())){
                log.info("Senha encontrada , acesso concedido");
            }else {
                throw new RuntimeException("Senha invalida");
            }
        }else{
            throw new RuntimeException("CPF não Encontrado");
        }
    }
}
