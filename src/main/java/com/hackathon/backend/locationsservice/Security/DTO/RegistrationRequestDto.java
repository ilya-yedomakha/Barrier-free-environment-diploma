package com.hackathon.backend.locationsservice.Security.DTO;

import lombok.Data;

@Data
public class RegistrationRequestDto {

    private String username;
    private String email;
    private String password;
}
