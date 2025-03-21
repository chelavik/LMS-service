package com.example.lms.controller;

import com.example.lms.dto.ProblemDto;
import com.example.lms.service.problem.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/problems")
@Tag(name = "problems", description = "Problems API")
public class ProblemController {
    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает информацию о задаче по её ID")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        Optional<ProblemDto> problemOpt = problemService.getProblemById(id);
        return problemOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её ID")
    public ResponseEntity<String> deleteProblem(@PathVariable Long id) {
        if (problemService.getProblemById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}
