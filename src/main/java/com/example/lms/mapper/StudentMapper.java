package com.example.lms.mapper;

import com.example.lms.model.Student;
import com.example.lms.dto.StudentDto;

import java.util.HashMap;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getLogin(),
                student.getFirstName(),
                student.getLastName(),
                student.getPhoneNumber(),
                student.getSolvedProblems().values().stream()
                        .map(ProblemMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public static Student toModel(StudentDto dto) {
        Student student = new Student(
                dto.getId(),
                dto.getLogin(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhoneNumber(),
                new HashMap<>()
        );

        dto.getSolvedProblems().forEach(problemDto ->
                student.getSolvedProblems().put(problemDto.getId(), ProblemMapper.toModel(problemDto)));

        return student;
    }
}
