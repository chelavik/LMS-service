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
public class CourseDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100, message = "size must be under 100")
    private String title;

    @NotNull(message = "must not be null")
    @Size(max = 3000, message = "size must be under 3000")
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<TopicDto> topics = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<StudentDto> students = new ArrayList<>();
}
