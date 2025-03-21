package com.example.lms.service.topic;

import com.example.lms.dto.TopicDto;

import java.util.Optional;

public interface TopicService {
    TopicDto addTopicToCourse(Long courseId, TopicDto topic);

    Optional<TopicDto> getTopicById(Long id);

    void deleteTopic(Long id);
}
