package com.hackathon.backend.locationsservice.Result;

public record Error(String code, String description) {
    public static final Error NONE = new Error("", "");
}
