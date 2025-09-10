package com.hackathon.backend.locationsservice.Controllers.RequestDTO.Read.Base;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseRegularReadDTO extends BaseReadDTO {
    public UUID id;
}
