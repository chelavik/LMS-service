package com.example.lms.model;

import lombok.*;

import java.util.HashMap;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private HashMap<Long, Problem> solvedProblems = new HashMap<>();

    public void addProblem(Problem problem) {
        solvedProblems.put(problem.getId(), problem);
    }
}
