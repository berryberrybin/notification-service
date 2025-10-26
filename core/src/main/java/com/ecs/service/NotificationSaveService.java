package com.ecs.service;

import com.ecs.domain.Notification;
import com.ecs.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationSaveService {

    private final NotificationRepository repository;

    public NotificationSaveService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void insert(Notification notification) {
        Notification result = repository.insert(notification);
        log.info("inserted: {}", result);
    }

    // insert or update (동일한 id 가진 알림이 이미 존재할 경우 update 수행)
    public void upsert(Notification notification) {
        Notification result = repository.save(notification);
        log.info("upserted: {}", result);
    }
}
