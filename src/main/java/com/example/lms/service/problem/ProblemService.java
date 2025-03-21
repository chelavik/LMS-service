package com.example.lms.service.problem;

import com.example.lms.dto.ProblemDto;

import java.util.Optional;

public interface ProblemService {
    ProblemDto addProblemToTopic(Long topicId, ProblemDto problem);

    Optional<ProblemDto> getProblemById(Long id);

    void deleteProblem(Long id);
}
