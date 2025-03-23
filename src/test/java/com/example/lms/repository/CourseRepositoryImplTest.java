package com.example.lms.repository;

import com.example.lms.model.Course;
import com.example.lms.repository.course.CourseRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryImplTest {
    private CourseRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new CourseRepositoryImpl();
    }

    @Test
    void testCreate() {
        Course course = new Course(0L, "course", "desc", new HashMap<>(), new HashMap<>());
        Course savedCourse = repository.create(course);

        assertNotNull(savedCourse);
        assertTrue(savedCourse.getId() > 0);
        assertEquals("course", savedCourse.getTitle());
    }

    @Test
    void testGetAll() {
        repository.create(new Course(0L, "title 1", "desc 1", new HashMap<>(), new HashMap<>()));
        repository.create(new Course(0L, "title 2", "desc 2", new HashMap<>(), new HashMap<>()));

        List<Course> courses = repository.getAll();

        assertEquals(2, courses.size());
    }

    @Test
    void testGetById_Success() {
        Course course = repository.create(new Course(0L, "title", "Text", new HashMap<>(), new HashMap<>()));

        Optional<Course> found = repository.get(course.getId());

        assertTrue(found.isPresent());
        assertEquals(course.getTitle(), found.get().getTitle());
    }

    @Test
    void testGetById_NotFound() {
        Optional<Course> found = repository.get(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testUpdate_Success() {
        Course course = repository.create(new Course(1L, "Old title", "Old desc", new HashMap<>(), new HashMap<>()));
        course.setTitle("New title");

        Course updated = repository.update(1L, course);

        assertEquals("New title", updated.getTitle());
    }

    @Test
    void testUpdate_NotFound() {
        Course course = new Course(999L, "title", "desc", new HashMap<>(), new HashMap<>());

        assertThrows(NoSuchElementException.class, () -> repository.update(999L, course));
    }

    @Test
    void testDelete() {
        Course course = repository.create(new Course(0L, "title", "desc", new HashMap<>(), new HashMap<>()));

        repository.delete(course.getId());

        assertFalse(repository.get(course.getId()).isPresent());
    }
}
