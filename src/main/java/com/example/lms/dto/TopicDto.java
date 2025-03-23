package com.example.lms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100, message = "size must be under 100")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 3000, message = "size must be under 3000")
    private String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ProblemDto> problems = new ArrayList<>();
}
