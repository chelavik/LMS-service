package com.example.lms.service.topic;

import com.example.lms.dto.TopicDto;
import com.example.lms.mapper.TopicMapper;
import com.example.lms.model.Course;
import com.example.lms.model.Topic;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.topic.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;

    @Override
    public TopicDto addTopicToCourse(Long courseId, TopicDto topicDto) {
        if (courseId == null || topicDto == null) {
            throw new IllegalArgumentException("Course ID and topic must not be null");
        }

        Optional<Course> courseOpt = courseRepository.get(courseId);
        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        Course course = courseOpt.get();
        Topic created = topicRepository.create(TopicMapper.toModel(topicDto));

        course.getTopics().put(created.getId(), created);
        courseRepository.update(courseId, course);

        return TopicMapper.toDto(created);
    }

    @Override
    public Optional<TopicDto> getTopicById(Long id) {
        return topicRepository.get(id).map(TopicMapper::toDto);
    }

    @Override
    public void deleteTopic(Long id) {
        for (Course course : courseRepository.getAll()) {
            course.getTopics().remove(id);
            courseRepository.update(course.getId(), course);
        }
        topicRepository.delete(id);
    }
}
