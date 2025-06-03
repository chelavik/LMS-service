package com.example.lms.repository.course;

import com.example.lms.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
    private final Map<Long, Course> courses = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Course> getAll() {
        return new ArrayList<>(courses.values());
    }

    @Override
    public Optional<Course> get(long id) {
        return Optional.ofNullable(courses.get(id));
    }

    @Override
    public Course create(Course entity) {
        entity.setId(nextId++);
        courses.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Course update(long id, Course entity) {
        if (!courses.containsKey(id)) {
            throw new NoSuchElementException("Course not found with id: " + entity.getId());
        }
        courses.put(id, entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        courses.remove(id);
    }
}
