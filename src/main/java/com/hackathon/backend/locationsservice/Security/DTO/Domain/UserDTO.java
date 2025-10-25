package com.hackathon.backend.locationsservice.Security.DTO.Domain;


import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String role
) {
}
