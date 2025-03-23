package com.example.lms.repository.student;

import com.example.lms.model.Problem;
import com.example.lms.model.Student;
import com.example.lms.repository.base.Repository;

public interface StudentRepository extends Repository<Student> {
    public Student updateTask(Long studentId, Problem problem);
}
