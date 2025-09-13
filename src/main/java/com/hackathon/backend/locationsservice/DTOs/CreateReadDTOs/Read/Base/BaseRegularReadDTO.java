package com.hackathon.backend.locationsservice.DTOs.CreateReadDTOs.Read.Base;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseRegularReadDTO extends BaseReadDTO {
    public UUID id;
}
