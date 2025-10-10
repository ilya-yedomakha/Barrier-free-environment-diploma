package com.hackathon.backend.locationsservice.Security.Controller;

import com.hackathon.backend.locationsservice.Security.DTO.Domain.UserDTO;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Services.UserService;
import com.hackathon.backend.locationsservice.Security.Services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/by-name/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username) {
        UserDTO user = userService.loadWholeUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
