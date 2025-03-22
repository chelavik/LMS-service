package com.example.lms.service.auth;

import com.example.lms.dto.UserDto;
import com.example.lms.model.User;

public interface AuthService {
    UserDto signIn(String login, User.Role role); // создание пользователя (только админом)

    UserDto authenticate(String authHeader); // проверка авторизации пользователя
}
