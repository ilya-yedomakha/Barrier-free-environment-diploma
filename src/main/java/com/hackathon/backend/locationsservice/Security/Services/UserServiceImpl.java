package com.hackathon.backend.locationsservice.Security.Services;

import com.hackathon.backend.locationsservice.Result.EntityErrors.EntityError;
import com.hackathon.backend.locationsservice.Result.EntityErrors.UserError;
import com.hackathon.backend.locationsservice.Result.Result;
import com.hackathon.backend.locationsservice.Security.DTO.Domain.UserDTO;
import com.hackathon.backend.locationsservice.Security.DTO.RoleUpdateRequest;
import com.hackathon.backend.locationsservice.Security.Domain.Role;
import com.hackathon.backend.locationsservice.Security.Domain.User;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + " was not found"));
    }

    public UserDTO loadWholeUserByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + " was not found"));

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }


    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return true;
        }
        return false;
    }

    public Result<User, UserDTO> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = loadWholeUserByUsername(username);
            userId = user.id();
        }

        final UUID currentUserId = userId;
        Optional<User> adminOpt = userRepository.findById(currentUserId);


        List<User> entities = userRepository.findAll();
        entities.remove(adminOpt.get());
        Result<User, UserDTO> res = Result.success();
        res.setEntities(entities);
        res.entityDTOs = entities.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();

        return res;
    }

    public Result<User, UserDTO> getUserById(UUID id) {
        Optional<User> entity = userRepository.findById(id);
        if (entity.isPresent()){
            Result<User, UserDTO> res = Result.success();
            res.setEntity(entity.get());
            User user = entity.get();
            res.setEntityDTO(new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()));
            return res;
        } else return Result.failure(EntityError.notFound(User.class,id));
    }

    public Result<User, UserDTO> getUserByUsername(String username) {
        Optional<User> entity = userRepository.findByUsername(username);
        if (entity.isPresent()){
            Result<User, UserDTO> res = Result.success();
            res.setEntity(entity.get());
            User user = entity.get();
            res.setEntityDTO(new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()));
            return res;
        } else return Result.failure(UserError.notFound(username));
    }

    public Result<User, UserDTO> updateUserRole(String username, RoleUpdateRequest role) {
        Optional<User> entity = userRepository.findByUsername(username);

        if (entity.isPresent()){
            Result<User, UserDTO> res = Result.success();
            User user = entity.get();
            user.setRole(Role.valueOf(role.getRole()));
            User savedUser = userRepository.save(user);
            res.setEntity(savedUser);
            res.setEntityDTO(new UserDTO(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getRole().name()));
            return res;
        } else return Result.failure(UserError.notFound(username));

    }
}
