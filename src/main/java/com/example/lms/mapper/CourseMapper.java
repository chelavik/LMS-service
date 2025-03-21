package com.example.lms.mapper;

import com.example.lms.model.Course;
import com.example.lms.dto.CourseDto;

import java.util.HashMap;
import java.util.stream.Collectors;

public class CourseMapper {
    public static CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getTopics().values().stream()
                        .map(TopicMapper::toDto)
                        .collect(Collectors.toList()),
                course.getStudents().values().stream()
                        .map(StudentMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public static Course toModel(CourseDto dto) {
        Course course = new Course(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                new HashMap<>(),
                new HashMap<>()
        );

        dto.getTopics().forEach(topicDto ->
                course.getTopics().put(topicDto.getId(), TopicMapper.toModel(topicDto)));

        dto.getStudents().forEach(studentDto ->
                course.getStudents().put(studentDto.getId(), StudentMapper.toModel(studentDto)));

        return course;
    }
}
