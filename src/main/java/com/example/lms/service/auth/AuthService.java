package com.example.lms.service.auth;

import com.example.lms.dto.UserDto;
import com.example.lms.model.User;

public interface AuthService {
    UserDto signIn(String login, User.Role role); // создание пользователя (только админом)

    UserDto authenticate(String authHeader); // проверка авторизации пользователя

    void checkAdminOrTeacherAccess(UserDto user); // проверка прав доступа админа или учителя

    void checkAdminAccess(UserDto currentUser);
}
