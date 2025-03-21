package com.example.lms.repository.problem;

import com.example.lms.model.Problem;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProblemRepositoryImpl implements ProblemRepository {
    private final Map<Long, Problem> problems = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Problem> getAll() {
        return new ArrayList<>(problems.values());
    }

    @Override
    public Optional<Problem> get(long id) {
        return Optional.ofNullable(problems.get(id));
    }

    @Override
    public Problem create(Problem entity) {
        entity.setId(nextId++);
        problems.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Problem update(long id, Problem entity) {
        if (!problems.containsKey(id)) {
            throw new NoSuchElementException("Problem not found with id: " + entity.getId());
        }
        problems.put(id, entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        problems.remove(id);
    }
}
