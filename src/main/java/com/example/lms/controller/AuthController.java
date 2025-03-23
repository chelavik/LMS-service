package com.example.lms.controller;

import com.example.lms.dto.UserDto;
import com.example.lms.exceptions.HttpStatusException;
import com.example.lms.exceptions.UnauthorizedException;
import com.example.lms.model.User;
import com.example.lms.service.auth.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "auth", description = "Authorization API")
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@RequestHeader("Authorization") String authHeader, @RequestParam String login, @RequestParam User.Role role) {
        if (login.isEmpty()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Login and role must not be null");
        }
        
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminAccess(currentUser);
            
            UserDto user = authService.signIn(login, role);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    public ResponseEntity<UserDto> authenticate(@RequestHeader("Authorization") String authHeader) {
        try {
            UserDto user = authService.authenticate(authHeader);
            return ResponseEntity.ok(user);
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } 
    }
}