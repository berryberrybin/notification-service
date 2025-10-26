package com.ecs.service;

import com.ecs.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationRemoveService {

    private final NotificationRepository repository;

    public NotificationRemoveService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void deleteById(String id) {
        log.info("deleted: {}", id);
        repository.deleteById(id);
    }
}
