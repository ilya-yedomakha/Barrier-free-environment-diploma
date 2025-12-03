package com.hackathon.backend.locationsservice.Services.EmailScope;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VerifyEmailCache {

    @CachePut(value = "emailCodes", key = "#email")
    public String saveCode(String email, String code) {
        return code; // Буде передано в кеш
    }

    @Cacheable(value = "emailCodes", key = "#email")
    public String getCode(String email) {
        return null; // неважливо, бо повертатиметься з кеша
    }
}

