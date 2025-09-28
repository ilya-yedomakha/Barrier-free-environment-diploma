package com.hackathon.backend.locationsservice.Result.EntityErrors;


import com.hackathon.backend.locationsservice.Result.Error;

import java.util.UUID;

public class EntityError {

    public static <T> Error sameName(Class<T> type, String name) {
        return new Error(
                type.getSimpleName() + ".SameName",
                type.getSimpleName() + " with name: " + name + " already exists"
        );
    }

    public static <T> Error notFound(Class<T> type, UUID id) {
        return new Error(
                type.getSimpleName() + ".NotFound",
                type.getSimpleName() + " with the id: " + id + " was not found"
        );
    }

    public static <T> Error notFound(Class<T> type, Long id) {
        return new Error(
                type.getSimpleName() + ".NotFound",
                type.getSimpleName() + " with the id: " + id + " was not found"
        );
    }

    public static <T> Error nullReference(Class<T> type) {
        return new Error(
                type.getSimpleName() + ".NullReference",
                type.getSimpleName() + " reference is required"
        );
    }

    public static <T> Error serverError(Class<T> type) {
        return new Error(
                type.getSimpleName() + ".ServerError",
                "Something went wrong!"
        );
    }

    public static <T> Error sameDesc(Class<T> type, String description) {
        return new Error(
                type.getSimpleName() + ".SameDesc",
                type.getSimpleName() + " with description: " + description + " already exists"
        );
    }
}

