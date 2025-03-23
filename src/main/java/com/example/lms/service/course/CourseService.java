package com.example.lms.service.course;

import com.example.lms.dto.CourseDto;

import java.util.Optional;

public interface CourseService {
    CourseDto createCourse(CourseDto course);

    Optional<CourseDto> getCourseById(Long id);

    void deleteCourse(Long id);

    void enrollStudent(Long courseId, Long studentId);

    void unenrollStudent(Long courseId, Long studentId);
}
