package com.example.AsyncFlow.demo.application.Usecase;

import com.example.AsyncFlow.demo.Domain.Model.Client;
import com.example.AsyncFlow.demo.application.Ports.DTO.ClientRegisterDto;
import com.example.AsyncFlow.demo.application.Ports.ClientRepository;
import com.example.AsyncFlow.demo.application.Ports.RegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
public class RegisterClientUseCasa  implements RegisterService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private ClientRepository clientRepository;

    public RegisterClientUseCasa(PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Override
    public void register(ClientRegisterDto clientregisterDto) {
        Client clientBD = new Client();

        boolean clientIsPresent = clientRepository.findbyid(clientregisterDto.getCpf()).isPresent();
        if (clientIsPresent){
            throw new RuntimeException("Usuario ja existente");
        }

        clientBD.setName(clientregisterDto.getName());
        clientBD.setCpf(clientregisterDto.getCpf());
        clientBD.setCep(clientregisterDto.getCep());
        clientBD.setEmail(clientregisterDto.getEmail());
        clientBD.setPassword(
                passwordEncoder.encode(clientregisterDto.getPassword())
        );

        try {
            clientRepository.save(clientBD);
        }catch (RuntimeException e){
            e.getMessage();
        }


    }
}
