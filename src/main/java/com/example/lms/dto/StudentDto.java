package com.example.lms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Size(max = 30, message = "size must be under 30")
    private String login;

    @NotEmpty
    @Size(max = 100, message = "size must be under 100")
    private String firstName;

    @NotEmpty
    @Size(max = 100, message = "size must be under 100")
    private String lastName;

    private String phoneNumber;

    @Valid
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProblemDto> solvedProblems = new ArrayList<>();
}
