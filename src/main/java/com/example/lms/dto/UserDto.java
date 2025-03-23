package com.example.lms.dto;

import com.example.lms.model.User.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    private String login;

    @NotNull(message = "must not be null")
    private String password;

    @NotNull(message = "must not be null")
    private Role role;
}


