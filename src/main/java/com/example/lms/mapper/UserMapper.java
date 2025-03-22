package com.example.lms.mapper;

import com.example.lms.model.User;
import com.example.lms.dto.UserDto;


public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getLogin(),
            user.getPassword(),
            user.getRole()
        );
    }

    public static User toModel(UserDto dto) {
        return new User(
            dto.getId(),
            dto.getLogin(),
            dto.getPassword(),
            dto.getRole()
        );
    }
}
