package com.example.lms.controller;

import com.example.lms.exceptions.HttpStatusException;
import com.example.lms.dto.StudentDto;
import com.example.lms.service.student.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "students", description = "Students API")
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует пользователя в ЛМС и возвращает информацию о нем")
    public ResponseEntity<StudentDto> registerStudent(@Valid @RequestBody StudentDto student) {
        try {
            StudentDto body = studentService.registerStudent(student);
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (IllegalStateException e) {

            throw new HttpStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PatchMapping("/{studentId}/solve/{problemId}")
    @Operation(summary = "Выполнить задание", description = "Добавляет задачу в выполненные для студента и возвращает информацию о нем")
    public ResponseEntity<StudentDto> updateStudentTask(@PathVariable Long studentId, @PathVariable Long problemId) {
        try {
            StudentDto body = studentService.updateStudentTask(studentId, problemId);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по ID")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        try {
            StudentDto body = studentService.getStudentById(id);
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
