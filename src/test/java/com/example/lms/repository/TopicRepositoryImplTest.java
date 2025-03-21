package com.example.lms.repository;

import com.example.lms.model.Topic;
import com.example.lms.repository.topic.TopicRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TopicRepositoryImplTest {
    private TopicRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new TopicRepositoryImpl();
    }

    @Test
    void testCreate() {
        Topic topic = new Topic(0L, "New Topic", "Some text", new HashMap<>());
        Topic savedTopic = repository.create(topic);

        assertNotNull(savedTopic);
        assertTrue(savedTopic.getId() > 0);
        assertEquals("New Topic", savedTopic.getTitle());
    }

    @Test
    void testGetAll() {
        repository.create(new Topic(0L, "Topic 1", "Text 1", new HashMap<>()));
        repository.create(new Topic(0L, "Topic 2", "Text 2", new HashMap<>()));

        List<Topic> topics = repository.getAll();

        assertEquals(2, topics.size());
    }

    @Test
    void testGetById_Success() {
        Topic topic = repository.create(new Topic(0L, "Find me", "Text", new HashMap<>()));

        Optional<Topic> found = repository.get(topic.getId());

        assertTrue(found.isPresent());
        assertEquals(topic.getTitle(), found.get().getTitle());
    }

    @Test
    void testGetById_NotFound() {
        Optional<Topic> found = repository.get(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testUpdate_Success() {
        Topic topic = repository.create(new Topic(1L, "Old title", "Old text", new HashMap<>()));
        topic.setTitle("New title");

        Topic updated = repository.update(1L, topic);

        assertEquals("New title", updated.getTitle());
    }

    @Test
    void testUpdate_NotFound() {
        Topic topic = new Topic(999L, "title", "text", new HashMap<>());

        assertThrows(NoSuchElementException.class, () -> repository.update(999L, topic));
    }

    @Test
    void testDelete() {
        Topic topic = repository.create(new Topic(0L, "title", "text", new HashMap<>()));

        repository.delete(topic.getId());

        assertFalse(repository.get(topic.getId()).isPresent());
    }
}
