package com.hackathon.backend.locationsservice.Security.Repositories;

import com.hackathon.backend.locationsservice.Security.Domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            SELECT t FROM Token t inner join User u
            on t.user.id = u.id
            where t.user.id = :userId and t.loggedOut = false
            """)
    List<Token> findAllAccessTokenByUser(UUID userId);

    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);
}
