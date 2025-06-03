package com.example.lms.controller;

import com.example.lms.dto.ProblemDto;
import com.example.lms.dto.UserDto;
import com.example.lms.exceptions.HttpStatusException;
import com.example.lms.exceptions.UnauthorizedException;
import com.example.lms.service.auth.AuthService;
import com.example.lms.service.problem.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/problems")
@Tag(name = "problems", description = "Problems API")
public class ProblemController {
    private final ProblemService problemService;
    private final AuthService authService;

    @Autowired
    public ProblemController(ProblemService problemService, AuthService authService) {
        this.problemService = problemService;
        this.authService = authService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает информацию о задаче по её ID")
    public ResponseEntity<ProblemDto> getProblemById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            authService.authenticate(authHeader);
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        Optional<ProblemDto> problemOpt = problemService.getProblemById(id);
        return problemOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её ID")
    public ResponseEntity<String> deleteProblem(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        if (problemService.getProblemById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}
