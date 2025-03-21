package com.example.lms.mapper;

import com.example.lms.model.Topic;
import com.example.lms.dto.TopicDto;

import java.util.HashMap;
import java.util.stream.Collectors;

public class TopicMapper {
    public static TopicDto toDto(Topic topic) {
        return new TopicDto(
                topic.getId(),
                topic.getTitle(),
                topic.getText(),
                topic.getProblems().values().stream()
                        .map(ProblemMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public static Topic toModel(TopicDto dto) {
        Topic topic = new Topic(
                dto.getId(),
                dto.getTitle(),
                dto.getText(),
                new HashMap<>()
        );

        dto.getProblems().forEach(problemDto ->
                topic.getProblems().put(problemDto.getId(), ProblemMapper.toModel(problemDto)));

        return topic;
    }
}
