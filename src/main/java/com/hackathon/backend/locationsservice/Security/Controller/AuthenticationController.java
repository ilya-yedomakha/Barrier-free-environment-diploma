package com.hackathon.backend.locationsservice.Security.Controller;

import com.hackathon.backend.locationsservice.Security.DTO.AuthenticationResponseDto;
import com.hackathon.backend.locationsservice.Security.DTO.LoginRequestDto;
import com.hackathon.backend.locationsservice.Security.DTO.RegistrationRequestDto;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import com.hackathon.backend.locationsservice.Security.Services.AuthenticationService;
import com.hackathon.backend.locationsservice.Security.Services.JwtService;
import com.hackathon.backend.locationsservice.Security.Services.UserServiceImpl;
import com.hackathon.backend.locationsservice.Services.EmailScope.EmailService;
import com.hackathon.backend.locationsservice.Services.EmailScope.VerifyEmailCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerifyEmailCache cache;

    public AuthenticationController(AuthenticationService authenticationService, UserServiceImpl userService,
                                    JwtService jwtService, UserRepository userRepository, EmailService emailService,
                                    VerifyEmailCache cache) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.cache = cache;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequestDto registrationDto) {

        if(userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("The username is already taken");
        }

        if(userService.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        authenticationService.register(registrationDto);

        return ResponseEntity.ok("Registration was successfull");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authenticationService.refreshToken(request, response);
    }

    @GetMapping("/validate/access")
    public ResponseEntity<Boolean> validateAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(false);
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            UserDetails user = userService.loadUserByUsername(username);

            boolean valid = jwtService.isValid(token, user);
            return ResponseEntity.ok(valid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/validate/refresh")
    public ResponseEntity<Boolean> validateRefreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(false);
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No user found"));

            userService.getUserByUsername(username);

            boolean valid = jwtService.isValidRefresh(token, user);
            return ResponseEntity.ok(valid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("email/verification/send")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        cache.saveCode(email, code);
        emailService.sendCode(email, code);

        return ResponseEntity.ok("code sent");
    }

    @PostMapping("email/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        String saved = cache.getCode(email);

        if (code.equals(saved)) return ResponseEntity.ok("verified");
        return ResponseEntity.badRequest().body("verification failed");
    }

}
