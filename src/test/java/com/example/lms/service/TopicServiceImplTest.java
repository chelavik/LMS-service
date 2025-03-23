package com.example.lms.service;

import com.example.lms.dto.CourseDto;
import com.example.lms.dto.TopicDto;
import com.example.lms.mapper.CourseMapper;
import com.example.lms.mapper.TopicMapper;
import com.example.lms.model.Course;
import com.example.lms.model.Topic;
import com.example.lms.repository.course.CourseRepository;
import com.example.lms.repository.topic.TopicRepository;
import com.example.lms.service.topic.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    void testAddTopicToCourse_Success() {
        long courseId = 1L;
        TopicDto topicDto = new TopicDto(null, "New Topic", "Some text", new ArrayList<>());
        Topic topicModel = TopicMapper.toModel(topicDto);
        topicModel.setId(1L);
        TopicDto createdTopicDto = TopicMapper.toDto(topicModel);

        CourseDto courseDto = new CourseDto(courseId, "Course 1", "Description", new ArrayList<>(), new ArrayList<>());
        Course courseModel = CourseMapper.toModel(courseDto);

        when(courseRepository.get(courseId)).thenReturn(Optional.of(courseModel));
        when(topicRepository.create(any(Topic.class))).thenReturn(topicModel);

        TopicDto savedTopic = topicService.addTopicToCourse(courseId, topicDto);

        assertNotNull(savedTopic);
        assertEquals(1L, savedTopic.getId());
        assertEquals(createdTopicDto.getTitle(), savedTopic.getTitle());
        assertEquals(topicModel, courseModel.getTopics().get(1L));
    }

    @Test
    void testAddTopicToCourse_CourseNotFound() {
        long courseId = 1L;
        TopicDto topic = new TopicDto(null, "New Topic", "Some text", new ArrayList<>());

        when(courseRepository.get(courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                topicService.addTopicToCourse(courseId, topic)
        );

        assertEquals("Course not found with id: " + courseId, exception.getMessage());
    }

    @Test
    void testAddTopicToCourse_NullArguments() {
        assertThrows(IllegalArgumentException.class, () -> topicService.addTopicToCourse(null, new TopicDto()));
        assertThrows(IllegalArgumentException.class, () -> topicService.addTopicToCourse(1L, null));
    }

    @Test
    void testGetTopicById_Success() {
        long topicId = 1L;
        TopicDto topicDto = new TopicDto(topicId, "Topic 1", "Some text", new ArrayList<>());
        Topic topicModel = TopicMapper.toModel(topicDto);

        when(topicRepository.get(topicId)).thenReturn(Optional.of(topicModel));

        Optional<TopicDto> foundTopic = topicService.getTopicById(topicId);

        assertTrue(foundTopic.isPresent());
        assertEquals(topicId, foundTopic.get().getId());
    }

    @Test
    void testGetTopicById_NotFound() {
        when(topicRepository.get(1L)).thenReturn(Optional.empty());

        Optional<TopicDto> foundTopic = topicService.getTopicById(1L);

        assertFalse(foundTopic.isPresent());
    }

    @Test
    void testDeleteTopic() {
        long topicId = 1L;

        assertDoesNotThrow(() -> topicService.deleteTopic(topicId));
    }
}
