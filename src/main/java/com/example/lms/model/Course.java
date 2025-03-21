package com.example.lms.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course {
    private Long id;
    private String title;
    private String description;
    private Map<Long, Topic> topics = new HashMap<>();
    private Map<Long, Student> students = new HashMap<>();
}
