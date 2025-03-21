package com.example.lms.service.problem;

import com.example.lms.dto.ProblemDto;
import com.example.lms.mapper.ProblemMapper;
import com.example.lms.model.Problem;
import com.example.lms.model.Topic;
import com.example.lms.repository.problem.ProblemRepository;
import com.example.lms.repository.topic.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;

    @Override
    public ProblemDto addProblemToTopic(Long topicId, ProblemDto problemDto) {
        if (topicId == null || problemDto == null) {
            throw new IllegalArgumentException("Topic ID and problem must not be null");
        }

        Optional<Topic> topicOpt = topicRepository.get(topicId);
        if (topicOpt.isEmpty()) {
            throw new IllegalArgumentException("Topic not found with id: " + topicId);
        }

        Topic topic = topicOpt.get();
        Problem created = problemRepository.create(ProblemMapper.toModel(problemDto));

        topic.getProblems().put(created.getId(), created);
        topicRepository.update(topicId, topic);

        return ProblemMapper.toDto(created);
    }

    @Override
    public Optional<ProblemDto> getProblemById(Long id) {
        return problemRepository.get(id).map(ProblemMapper::toDto);
    }

    @Override
    public void deleteProblem(Long id) {
        for (Topic topic : topicRepository.getAll()) {
            topic.getProblems().remove(id);
            topicRepository.update(topic.getId(), topic);
        }
        problemRepository.delete(id);
    }
}
