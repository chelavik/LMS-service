package com.example.lms.service.student;

import com.example.lms.dto.StudentDto;
import com.example.lms.mapper.StudentMapper;
import com.example.lms.model.Course;
import com.example.lms.model.Problem;
import com.example.lms.model.Student;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.problem.ProblemRepository;
import com.example.lms.repository.student.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ProblemRepository problemRepository;
    private final CourseRepository courseRepository;

    @Override
    public StudentDto registerStudent(StudentDto studentDto) {
        Student model = StudentMapper.toModel(studentDto);
        Student created = studentRepository.create(model);
        return StudentMapper.toDto(created);
    }

    @Override
    public StudentDto updateStudentTask(Long studentId, Long problemId) {
        Optional<Problem> problemOpt = problemRepository.get(problemId);
        if (problemOpt.isEmpty()) {
            throw new IllegalArgumentException("Problem with id=%s not found".formatted(problemId));
        }

        Student updated = studentRepository.updateTask(studentId, problemOpt.get());
        return StudentMapper.toDto(updated);
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student updated = studentRepository.update(id, StudentMapper.toModel(studentDto));
        return StudentMapper.toDto(updated);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Optional<Student> result = studentRepository.get(id);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Student with id=%s not found".formatted(id));
        }
        return StudentMapper.toDto(result.get());
    }

    @Override
    public void deleteStudent(Long id) {
        for (Course course : courseRepository.getAll()) {
            course.getStudents().remove(id);
            courseRepository.update(course.getId(), course);
        }
        studentRepository.delete(id);
    }
}
