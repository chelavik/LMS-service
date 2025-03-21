package com.example.lms.service;

import com.example.lms.dto.ProblemDto;
import com.example.lms.dto.TopicDto;
import com.example.lms.mapper.ProblemMapper;
import com.example.lms.mapper.TopicMapper;
import com.example.lms.model.Problem;
import com.example.lms.model.Topic;
import com.example.lms.repository.problem.ProblemRepository;
import com.example.lms.repository.topic.TopicRepository;
import com.example.lms.service.problem.ProblemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProblemServiceImplTest {

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private ProblemServiceImpl problemService;

    @Test
    void testAddProblemToTopic_Success() {
        long topicId = 1L;
        ProblemDto problemDto = new ProblemDto(null, "New Problem", "Description");
        Problem problemModel = ProblemMapper.toModel(problemDto);
        problemModel.setId(1L);

        ProblemDto createdProblemDto = ProblemMapper.toDto(problemModel);

        TopicDto topicDto = new TopicDto(topicId, "Topic 1", "Text", new ArrayList<>());
        Topic topicModel = TopicMapper.toModel(topicDto);

        when(topicRepository.get(topicId)).thenReturn(Optional.of(topicModel));
        when(problemRepository.create(any(Problem.class))).thenReturn(problemModel);

        ProblemDto savedProblem = problemService.addProblemToTopic(topicId, problemDto);

        assertNotNull(savedProblem);
        assertEquals(1L, savedProblem.getId());
        assertEquals(problemModel, topicModel.getProblems().get(1L));
    }

    @Test
    void testAddProblemToTopic_TopicNotFound() {
        long topicId = 1L;
        ProblemDto problemDto = new ProblemDto(null, "New Problem", "Description");

        when(topicRepository.get(topicId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                problemService.addProblemToTopic(topicId, problemDto)
        );

        assertEquals("Topic not found with id: " + topicId, exception.getMessage());
    }

    @Test
    void testAddProblemToTopic_NullArguments() {
        assertThrows(IllegalArgumentException.class, () -> problemService.addProblemToTopic(null, new ProblemDto()));
        assertThrows(IllegalArgumentException.class, () -> problemService.addProblemToTopic(1L, null));
    }

    @Test
    void testGetProblemById_Success() {
        Long problemId = 1L;
        ProblemDto problemDto = new ProblemDto(problemId, "Problem 1", "Description");
        Problem problemModel = ProblemMapper.toModel(problemDto);

        when(problemRepository.get(problemId)).thenReturn(Optional.of(problemModel));

        Optional<ProblemDto> foundProblem = problemService.getProblemById(problemId);

        assertTrue(foundProblem.isPresent());
        assertEquals(problemId, foundProblem.get().getId());
    }

    @Test
    void testGetProblemById_NotFound() {
        when(problemRepository.get(1L)).thenReturn(Optional.empty());

        Optional<ProblemDto> foundProblem = problemService.getProblemById(1L);

        assertFalse(foundProblem.isPresent());
    }

    @Test
    void testDeleteProblem() {
        long problemId = 1L;

        assertDoesNotThrow(() -> problemService.deleteProblem(problemId));
    }
}
