package com.hackathon.backend.locationsservice.Security.DTO.Domain;

public record UserCreateDTO(
        String username,
        String password,
        String email,
        String role
) {
}
