package com.hackathon.backend.locationsservice.Domain.Core.Base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEntity extends BaseEntity {
    @NotNull
    @NotBlank
    @Column(length = 255, nullable = false)
    private String name;
}
