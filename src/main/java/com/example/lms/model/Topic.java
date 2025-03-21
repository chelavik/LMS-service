package com.example.lms.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Topic {
    private Long id;
    private String title;
    private String text;
    private Map<Long, Problem> problems = new HashMap<>();
}
