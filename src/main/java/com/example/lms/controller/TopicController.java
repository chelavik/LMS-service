package com.example.lms.controller;

import com.example.lms.exceptions.HttpStatusException;
import com.example.lms.dto.ProblemDto;
import com.example.lms.dto.TopicDto;
import com.example.lms.service.problem.ProblemService;
import com.example.lms.service.topic.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/topics")
@Tag(name = "topics", description = "Topics API")
public class TopicController {
    private final TopicService topicService;
    private final ProblemService problemService;

    @Autowired
    public TopicController(TopicService topicService, ProblemService problemService) {
        this.topicService = topicService;
        this.problemService = problemService;
    }

    @PostMapping("/{id}/problems")
    @Operation(summary = "Добавить задачу в тему", description = "Создает новую задачу и добавляет её в указанную тему")
    public ResponseEntity<ProblemDto> addProblemToTopic(@PathVariable Long id, @RequestBody ProblemDto problem) {
        try {
            ProblemDto createdProblem = problemService.addProblemToTopic(id, problem);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProblem);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().startsWith("Topic not found")) {
                throw new HttpStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тему по ID", description = "Возвращает информацию о теме по её ID")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Long id) {
        Optional<TopicDto> topicOpt = topicService.getTopicById(id);
        return topicOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тему", description = "Удаляет тему по её ID")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        if (topicService.getTopicById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
