package com.example.lms.mapper;

import com.example.lms.model.Problem;
import com.example.lms.dto.ProblemDto;

public class ProblemMapper {
    public static ProblemDto toDto(Problem problem) {
        return new ProblemDto(
                problem.getId(),
                problem.getTitle(),
                problem.getDescription()
        );
    }

    public static Problem toModel(ProblemDto dto) {
        return new Problem(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription()
        );
    }
}
