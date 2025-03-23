package com.example.lms.repository.topic;

import com.example.lms.model.Topic;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TopicRepositoryImpl implements TopicRepository {
    private final Map<Long, Topic> topics = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Topic> getAll() {
        return new ArrayList<>(topics.values());
    }

    @Override
    public Optional<Topic> get(long id) {
        return Optional.ofNullable(topics.get(id));
    }

    @Override
    public Topic create(Topic entity) {
        entity.setId(nextId++);
        topics.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Topic update(long id, Topic entity) {
        if (!topics.containsKey(id)) {
            throw new NoSuchElementException("Topic not found with id: " + entity.getId());
        }
        topics.put(id, entity);
        return entity;
    }

    @Override
    public void delete(long id) {
        topics.remove(id);
    }
}
