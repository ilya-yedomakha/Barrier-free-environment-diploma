package com.hackathon.backend.locationsservice.Security.DTO;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String username;
    private String password;
}
