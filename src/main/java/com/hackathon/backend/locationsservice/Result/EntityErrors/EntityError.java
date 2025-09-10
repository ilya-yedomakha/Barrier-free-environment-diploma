package com.hackathon.backend.locationsservice.Result.EntityErrors;


import com.hackathon.backend.locationsservice.Domain.Core.Base.BaseEntity;
import com.hackathon.backend.locationsservice.Result.Error;

import java.util.UUID;

public class EntityError<T extends BaseEntity> {

    private final Class<T> type;

    public EntityError(Class<T> type) {
        this.type = type;
    }

    public Error sameName(String name) {
        return new Error(
                type.getSimpleName() + ".SameName",
                type.getSimpleName() + " with name: " + name + " already exists"
        );
    }

    public Error notFound(UUID id) {
        return new Error(
                type.getSimpleName() + ".NotFound",
                type.getSimpleName() + " with the id: " + id + " was not found"
        );
    }

    public Error nullReference() {
        return new Error(
                type.getSimpleName() + ".NullReference",
                type.getSimpleName() + " reference is required"
        );
    }

    public Error serverError() {
        return new Error(
                type.getSimpleName() + ".ServerError",
                "Something went wrong!"
        );
    }
}

