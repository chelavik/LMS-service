package com.example.lms.service;

import com.example.lms.dto.StudentDto;
import com.example.lms.dto.UserDto;
import com.example.lms.exceptions.UnauthorizedException;
import com.example.lms.mapper.StudentMapper;
import com.example.lms.model.Student;
import com.example.lms.model.User;
import com.example.lms.repository.student.StudentRepository;
import com.example.lms.repository.user.UserRepository;
import com.example.lms.service.auth.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private Student student;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("testUser");
        user.setPassword("testPassword");
        user.setRole(User.Role.STUDENT);

        student = StudentMapper.toModel(new StudentDto(null, "testUser", "temporaryName", "temporarySurname", null, new ArrayList<>()));
    }

    @Test
    void testAuthenticate_Success() {
        String login = "testUser";
        String password = "testPassword";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        UserDto result = authService.authenticate(authHeader);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
        assertEquals(password, result.getPassword());
        assertEquals(User.Role.STUDENT, result.getRole());

        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void testAuthenticate_InvalidHeader() {
        String invalidAuthHeader = "InvalidHeader";

        assertThrows(UnauthorizedException.class, () -> authService.authenticate(invalidAuthHeader));

        verify(userRepository, never()).findByLogin(anyString());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        String login = "nonExistentUser";
        String password = "testPassword";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.authenticate(authHeader));

        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void testAuthenticate_InvalidPassword() {
        String login = "testUser";
        String wrongPassword = "wrongPassword";
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((login + ":" + wrongPassword).getBytes(StandardCharsets.UTF_8));

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> authService.authenticate(authHeader));

        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void testSignIn_Success() {
        String login = "newUser";
        User.Role role = User.Role.STUDENT;

        User newUser = new User();
        newUser.setId(2L);
        newUser.setLogin(login);
        newUser.setPassword(UUID.randomUUID().toString());
        newUser.setRole(role);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(userRepository.create(any(User.class))).thenReturn(newUser);
        when(studentRepository.create(any(Student.class))).thenReturn(student);

        UserDto result = authService.signIn(login, role);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
        assertEquals(User.Role.STUDENT, result.getRole());

        verify(userRepository, times(1)).findByLogin(login);
        verify(userRepository, times(1)).create(any(User.class));
        verify(studentRepository, times(1)).create(any(Student.class));
    }

    @Test
    void testSignIn_UserAlreadyExists() {
        String login = "existingUser";
        User.Role role = User.Role.STUDENT;

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authService.signIn(login, role));

        verify(userRepository, never()).create(any(User.class));
        verify(studentRepository, never()).create(any(Student.class));
    }

}