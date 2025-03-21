package com.example.lms.service.course;

import com.example.lms.dto.CourseDto;
import com.example.lms.mapper.CourseMapper;
import com.example.lms.model.Course;
import com.example.lms.model.Student;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.student.StudentRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        if (courseDto.getTitle() == null || courseDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (courseDto.getDescription() == null || courseDto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Course description cannot be empty");
        }

        Course model = CourseMapper.toModel(courseDto);
        Course saved = courseRepository.create(model);
        return CourseMapper.toDto(saved);
    }

    @Override
    public Optional<CourseDto> getCourseById(Long id) {
        return courseRepository.get(id).map(CourseMapper::toDto);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.delete(id);
    }

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        Optional<Course> courseOpt = courseRepository.get(courseId);
        Optional<Student> studentOpt = studentRepository.get(studentId);

        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        Course course = courseOpt.get();
        Student student = studentOpt.get();

        if (!course.getStudents().containsKey(studentId)) {
            course.getStudents().put(studentId, student);
            courseRepository.update(courseId, course);
        } else {
            throw new IllegalArgumentException("Student already enrolled");
        }
    }

    @Override
    public void unenrollStudent(Long courseId, Long studentId) {
        Optional<Course> courseOpt = courseRepository.get(courseId);
        Optional<Student> studentOpt = studentRepository.get(studentId);

        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found");
        }

        Course course = courseOpt.get();

        if (course.getStudents().containsKey(studentId)) {
            course.getStudents().remove(studentId);
            courseRepository.update(courseId, course);
        } else {
            throw new IllegalArgumentException("Student not enrolled in course");
        }
    }
}
