package com.example.lms.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Problem {
    private Long id;
    private String title;
    private String description;
}
