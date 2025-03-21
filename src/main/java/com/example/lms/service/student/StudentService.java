package com.example.lms.service.student;

import com.example.lms.dto.StudentDto;

public interface StudentService {
    StudentDto registerStudent(StudentDto student);

    StudentDto updateStudentTask(Long studentId, Long problemId);

    StudentDto updateStudent(Long id, StudentDto student);

    StudentDto getStudentById(Long id);

    void deleteStudent(Long id);
}
