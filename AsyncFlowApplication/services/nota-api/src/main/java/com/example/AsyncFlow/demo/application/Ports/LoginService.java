package com.example.AsyncFlow.demo.application.Ports;

import com.example.AsyncFlow.demo.application.Ports.DTO.ClientLoginDto;

public interface LoginService {
    void login(ClientLoginDto clientLoginDto);
}
