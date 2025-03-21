package com.example.lms.repository;

import com.example.lms.model.Problem;
import com.example.lms.model.Student;
import com.example.lms.repository.student.StudentRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryImplTest {
    private StudentRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepositoryImpl();
    }

    @Test
    void testCreate() {
        Student student = new Student(null, "login1", "John", "Doe", "1234567890", new HashMap<>());
        Student createdStudent = repository.create(student);

        assertNotNull(createdStudent.getId());
        assertEquals("login1", createdStudent.getLogin());
        assertEquals("John", createdStudent.getFirstName());
    }

    @Test
    void testGetAll() {
        repository.create(new Student(null, "login1", "John", "Doe", "1234567890", new HashMap<>()));
        repository.create(new Student(null, "login2", "Jane", "Smith", "0987654321", new HashMap<>()));

        List<Student> students = repository.getAll();
        assertEquals(2, students.size());
    }

    @Test
    void testGetById() {
        Student student = repository.create(new Student(null, "login1", "John", "Doe", "1234567890", new HashMap<>()));
        Optional<Student> found = repository.get(student.getId());

        assertTrue(found.isPresent());
        assertEquals("login1", found.get().getLogin());
    }

    @Test
    void testUpdateTask() {
        Student student = repository.create(new Student(null, "login1", "John", "Doe", "1234567890", new HashMap<>()));
        Problem problem = new Problem(1L, "Math Problem", "Solve the equation");

        Student updatedStudent = repository.updateTask(student.getId(), problem);

        assertFalse(updatedStudent.getSolvedProblems().isEmpty());
        assertEquals("Math Problem", updatedStudent.getSolvedProblems().get(1L).getTitle());
    }
}
