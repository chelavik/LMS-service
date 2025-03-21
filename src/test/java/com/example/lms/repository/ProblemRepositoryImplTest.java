package com.example.lms.repository;

import com.example.lms.model.Problem;
import com.example.lms.repository.problem.ProblemRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProblemRepositoryImplTest {
    private ProblemRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProblemRepositoryImpl();
    }

    @Test
    void testCreate() {
        Problem problem = new Problem(null, "Problem", "Desc");
        Problem createdProblem = repository.create(problem);

        assertNotNull(createdProblem.getId());
        assertEquals("Problem", createdProblem.getTitle());
        assertEquals("Desc", createdProblem.getDescription());
    }

    @Test
    void testGetAll() {
        repository.create(new Problem(null, "Problem 1", "Desc 1"));
        repository.create(new Problem(null, "Problem 2", "Desc 2"));

        List<Problem> problems = repository.getAll();
        assertEquals(2, problems.size());

        assertEquals("Problem 1", problems.get(0).getTitle());
        assertEquals("Desc 1", problems.get(0).getDescription());

        assertEquals("Problem 2", problems.get(1).getTitle());
        assertEquals("Desc 2", problems.get(1).getDescription());
    }

    @Test
    void testGetById() {
        Problem problem = repository.create(new Problem(null, "Problem", "Desc"));

        Optional<Problem> found = repository.get(problem.getId());
        assertTrue(found.isPresent());
        assertEquals("Problem", found.get().getTitle());
    }

    @Test
    void testUpdate() {
        Problem problem = repository.create(new Problem(null, "Old title", "Old desc"));
        problem.setTitle("New title");

        Problem updated = repository.update(1L, problem);
        assertEquals("New title", updated.getTitle());
        assertEquals("Old desc", updated.getDescription());
    }

    @Test
    void testUpdateNotFound() {
        Problem problem = new Problem(999L, "Problem", "Desc");

        assertThrows(NoSuchElementException.class, () -> repository.update(999L, problem));
    }

    @Test
    void testDelete() {
        Problem problem = repository.create(new Problem(null, "Problem", "Desc"));
        repository.delete(problem.getId());

        assertFalse(repository.get(problem.getId()).isPresent());
    }
}
