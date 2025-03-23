package com.example.lms.service;

import com.example.lms.dto.ProblemDto;
import com.example.lms.dto.StudentDto;
import com.example.lms.mapper.ProblemMapper;
import com.example.lms.mapper.StudentMapper;
import com.example.lms.model.Problem;
import com.example.lms.model.Student;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.problem.ProblemRepository;
import com.example.lms.repository.student.StudentRepository;
import com.example.lms.repository.user.UserRepository;
import com.example.lms.service.student.StudentServiceImpl;
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
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testRegisterStudent_Success() {
        StudentDto studentDto = new StudentDto(1L, "login", "John", "Doe", "1234567890", new ArrayList<>());
        Student studentModel = StudentMapper.toModel(studentDto);

        when(studentRepository.create(any(Student.class))).thenReturn(studentModel);

        StudentDto registered = studentService.registerStudent(studentDto);

        assertNotNull(registered);
        assertEquals(studentDto.getId(), registered.getId());
        assertEquals(studentDto.getFirstName(), registered.getFirstName());
        verify(studentRepository, times(1)).create(any(Student.class));
    }

    @Test
    void testUpdateStudentTask_Success() {
        long studentId = 1L;
        long problemId = 100L;

        ProblemDto problemDto = new ProblemDto(problemId, "Sample Problem", "Problem description");
        Problem problemModel = ProblemMapper.toModel(problemDto);

        Student studentModel = new Student(studentId, "login", "John", "Doe", "1234567890", new HashMap<>());

        when(problemRepository.get(problemId)).thenReturn(Optional.of(problemModel));
        when(studentRepository.updateTask(studentId, problemModel)).thenReturn(studentModel);

        StudentDto result = studentService.updateStudentTask(studentId, problemId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        verify(studentRepository, times(1)).updateTask(studentId, problemModel);
    }

    @Test
    void testUpdateStudentTask_ProblemNotFound() {
        long studentId = 1L;
        long problemId = 100L;

        when(problemRepository.get(problemId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                studentService.updateStudentTask(studentId, problemId)
        );

        assertEquals("Problem with id=100 not found", exception.getMessage());
    }

    @Test
    void testUpdateStudent_Success() {
        long studentId = 1L;
        StudentDto studentDto = new StudentDto(studentId, "login", "John", "Doe", "1234567890", new ArrayList<>());
        Student studentModel = StudentMapper.toModel(studentDto);

        when(studentRepository.update(eq(studentId), any(Student.class))).thenReturn(studentModel);

        StudentDto updated = studentService.updateStudent(studentId, studentDto);

        assertNotNull(updated);
        assertEquals(studentId, updated.getId());
        assertEquals("John", updated.getFirstName());
        verify(studentRepository, times(1)).update(eq(studentId), any(Student.class));
    }

    @Test
    void testGetStudentById_Success() {
        long studentId = 1L;
        Student studentModel = new Student(studentId, "login", "John", "Doe", "1234567890", new HashMap<>());

        when(studentRepository.get(studentId)).thenReturn(Optional.of(studentModel));

        StudentDto result = studentService.getStudentById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetStudentById_StudentNotFound() {
        long studentId = 1L;

        when(studentRepository.get(studentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                studentService.getStudentById(studentId)
        );

        assertEquals("Student with id=1 not found", exception.getMessage());
    }

    @Test
    void testDeleteStudent() {
        long studentId = 1L;

        doNothing().when(studentRepository).delete(studentId);

        assertDoesNotThrow(() -> studentService.deleteStudent(studentId));
        verify(studentRepository, times(1)).delete(studentId);
    }
}
