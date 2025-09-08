package com.hackathon.backend.locationsservice.Domain.Core;

import jakarta.persistence.*;

import java.util.UUID;

@Table(name="users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
