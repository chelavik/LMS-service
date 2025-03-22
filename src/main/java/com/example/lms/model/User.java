package com.example.lms.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String login;
    private String password;
    private Role role;

    public enum Role {
        ADMIN, TEACHER, STUDENT
    }
}
