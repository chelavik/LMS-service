package com.example.lms.repository.student;

import com.example.lms.model.Problem;
import com.example.lms.model.Student;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final Map<Long, Student> students = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Optional<Student> get(long id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public Student create(Student entity) {
        entity.setId(nextId++);
        students.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Student update(long id, Student entity) {
        if (!students.containsKey(id)) {
            throwNotFound(id);
        }

        Student existingStudent = students.get(id);
        if (entity.getLogin() != null) {
            existingStudent.setLogin(entity.getLogin());
        }
        if (entity.getFirstName() != null) {
            existingStudent.setFirstName(entity.getFirstName());
        }
        if (entity.getLastName() != null) {
            existingStudent.setLastName(entity.getLastName());
        }
        if (entity.getPhoneNumber() != null) {
            existingStudent.setPhoneNumber(entity.getPhoneNumber());
        }

        students.put(id, existingStudent);
        return students.get(id);
    }

    @Override
    public void delete(long id) {
        if (!students.containsKey(id)) {
            throwNotFound(id);
        }

        students.remove(id);
    }

    @Override
    public Student updateTask(Long studentId, Problem problem) {
        if (!students.containsKey(studentId)) {
            throwNotFound(studentId);
        }

        Student student = students.get(studentId);
        student.addProblem(problem);
        students.put(studentId, student);

        return students.get(studentId);
    }

    private void throwNotFound(Long id) {
        throw new IllegalArgumentException("Student with id=%s not found".formatted(id));
    }
}
