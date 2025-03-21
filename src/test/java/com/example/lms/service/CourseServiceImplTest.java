package com.example.lms.service;

import com.example.lms.dto.CourseDto;
import com.example.lms.mapper.CourseMapper;
import com.example.lms.model.Course;
import com.example.lms.model.Student;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.student.StudentRepository;
import com.example.lms.service.course.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void testCreateCourse_Success() {
        CourseDto courseDto = new CourseDto(null, "title", "desc", new ArrayList<>(), new ArrayList<>());
        Course model = CourseMapper.toModel(courseDto);
        model.setId(1L);

        when(courseRepository.create(any(Course.class))).thenReturn(model);

        CourseDto savedCourse = courseService.createCourse(courseDto);

        assertNotNull(savedCourse);
        assertEquals(1L, savedCourse.getId());
    }

    @Test
    void testCreateCourse_EmptyTitle() {
        CourseDto course = new CourseDto(null, "", "desc", new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(course));
    }

    @Test
    void testCreateCourse_EmptyDescription() {
        CourseDto course = new CourseDto(null, "title", "", new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(course));
    }

    @Test
    void testGetCourseById_Success() {
        Course model = new Course(1L, "title", "desc", new HashMap<>(), new HashMap<>());
        when(courseRepository.get(1L)).thenReturn(Optional.of(model));

        Optional<CourseDto> result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals("title", result.get().getTitle());
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.get(1L)).thenReturn(Optional.empty());

        Optional<CourseDto> result = courseService.getCourseById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteCourse_Success() {
        doNothing().when(courseRepository).delete(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(1L);
    }

    @Test
    void testDeleteCourse_NotFound() {
        assertDoesNotThrow(() -> courseService.deleteCourse(1L));

        verify(courseRepository, times(1)).delete(1L);
    }

    @Test
    void testEnrollStudent_Success() {
        Course course = new Course(1L, "title", "desc", new HashMap<>(), new HashMap<>());
        Student student = new Student(1L, "login", "rustam", "khamidullin", "+78005553535", new HashMap<>());

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.of(student));

        courseService.enrollStudent(1L, 1L);

        assertTrue(course.getStudents().containsKey(1L));
        verify(courseRepository, times(1)).update(1L, course);
    }

    @Test
    void testEnrollStudent_CourseNotFound() {
        when(courseRepository.get(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.enrollStudent(1L, 1L));
    }

    @Test
    void testEnrollStudent_StudentNotFound() {
        Course course = new Course(1L, "title", "desc", new HashMap<>(), new HashMap<>());

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.enrollStudent(1L, 1L));
    }

    @Test
    void testEnrollStudent_StudentAlreadyEnrolled() {
        Student student = new Student(1L, "login", "rustam", "khamidullin", "+78005553535", new HashMap<>());
        HashMap<Long, Student> students = new HashMap<>();
        students.put(1L, student);
        Course course = new Course(1L, "title", "desc", new HashMap<>(), students);

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.of(student));

        assertThrows(IllegalArgumentException.class, () -> courseService.enrollStudent(1L, 1L));
    }

    @Test
    void testUnenrollStudent_Success() {
        Student student = new Student(1L, "login", "rustam", "khamidullin", "+78005553535", new HashMap<>());
        HashMap<Long, Student> students = new HashMap<>();
        students.put(1L, student);
        Course course = new Course(1L, "title", "desc", new HashMap<>(), students);

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.of(student));

        courseService.unenrollStudent(1L, 1L);

        assertFalse(course.getStudents().containsKey(1L));
        verify(courseRepository, times(1)).update(1L, course);
    }

    @Test
    void testUnenrollStudent_CourseNotFound() {
        when(courseRepository.get(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.unenrollStudent(1L, 1L));
    }

    @Test
    void testUnenrollStudent_StudentNotFound() {
        Course course = new Course(1L, "title", "desc", new HashMap<>(), new HashMap<>());

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.unenrollStudent(1L, 1L));
    }

    @Test
    void testUnenrollStudent_StudentNotEnrolled() {
        Course course = new Course(1L, "title", "desc", new HashMap<>(), new HashMap<>());
        Student student = new Student(1L, "login", "rustam", "khamidullin", "+78005553535", new HashMap<>());

        when(courseRepository.get(1L)).thenReturn(Optional.of(course));
        when(studentRepository.get(1L)).thenReturn(Optional.of(student));

        assertThrows(IllegalArgumentException.class, () -> courseService.unenrollStudent(1L, 1L));
    }
}
