package com.example.lms.service.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.lms.dto.UserDto;
import com.example.lms.exceptions.UnauthorizedException;
import com.example.lms.mapper.UserMapper;
import com.example.lms.model.User;
import com.example.lms.model.User.Role;
import com.example.lms.repository.user.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;

    @Override
    public UserDto authenticate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new UnauthorizedException("Invalid authorization header");
        }
        
        String base64Credentials = authHeader.substring("Basic".length()).trim();

        if (base64Credentials.isEmpty() || base64Credentials.length() % 4 != 0) {
            throw new UnauthorizedException("Invalid Base64 credentials");
        }

        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        String[] loginAndPassword = credentials.split(":", 2);

        if (loginAndPassword.length != 2) {
            throw new UnauthorizedException("Invalid authorization header");
        }

        String login = loginAndPassword[0];
        String password = loginAndPassword[1];

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid password");
        }

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto signIn(String login, User.Role role) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("User with login=%s already exists".formatted(login));
        }
        if (role != User.Role.ADMIN && role != User.Role.TEACHER && role != User.Role.STUDENT) {
                throw new IllegalArgumentException("Invalid role");
        }
        

        String password = UUID.randomUUID().toString();

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);

        User savedUser = userRepository.create(user);

        return UserMapper.toDto(savedUser);
    }

    public void checkAdminOrTeacherAccess(UserDto user) {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.TEACHER) {
            throw new UnauthorizedException("Access denied. Only admins and teachers can perform this action.");
        }
    }

    public void checkAdminAccess(UserDto user) {
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Access denied. Only admins can perform this action.");
        }
    }
}
