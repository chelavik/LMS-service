package com.example.lms.controller;

import com.example.lms.exceptions.HttpStatusException;
import com.example.lms.exceptions.UnauthorizedException;
import com.example.lms.dto.CourseDto;
import com.example.lms.dto.TopicDto;
import com.example.lms.dto.UserDto;
import com.example.lms.service.auth.AuthService;
import com.example.lms.service.course.CourseService;
import com.example.lms.service.topic.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "courses", description = "Courses API")
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final TopicService topicService;
    private final AuthService authService;

    @Autowired
    public CourseController(CourseService courseService, TopicService topicService, AuthService authService) {
        this.authService = authService;
        this.courseService = courseService;
        this.topicService = topicService;
    }

    @PostMapping
    @Operation(summary = "Создание нового курса")
    public ResponseEntity<CourseDto> createCourse(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid CourseDto course) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);    
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        CourseDto createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение курса по Id")
    public ResponseEntity<CourseDto> getCourseById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            authService.authenticate(authHeader);  
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        CourseDto course = courseService.getCourseById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "Course not found with id: " + id));
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление курса по Id")
    public ResponseEntity<CourseDto> deleteCourse(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    @Operation(summary = "Добавление студента в курс по Id")
    public ResponseEntity<CourseDto> enrollStudent(@RequestHeader("Authorization") String authHeader, @PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);

            courseService.enrollStudent(courseId, studentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else {
                throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{courseId}/unenroll/{studentId}")
    @Operation(summary = "Удаление студента с курса по Id")
    public ResponseEntity<CourseDto> unenrollStudent(@RequestHeader("Authorization") String authHeader, @PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);

            courseService.unenrollStudent(courseId, studentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else {
                throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{courseId}/topics")
    @Operation(summary = "Добавление темы в курс по Id")
    public ResponseEntity<TopicDto> addTopicToCourse(@RequestHeader("Authorization") String authHeader, @PathVariable Long courseId, @Valid @RequestBody TopicDto topic) {
        try {
            UserDto currentUser = authService.authenticate(authHeader);
            authService.checkAdminOrTeacherAccess(currentUser);

            TopicDto addedTopic = topicService.addTopicToCourse(courseId, topic);
            return ResponseEntity.ok(addedTopic);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
