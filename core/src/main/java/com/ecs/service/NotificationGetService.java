package com.ecs.service;

import com.ecs.domain.Notification;
import com.ecs.domain.NotificationType;
import com.ecs.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@Slf4j
public class NotificationGetService {

    private final NotificationRepository repository;

    public NotificationGetService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Optional<Notification> getNotificationByTypeAndCommentId(NotificationType type, long commentId) {
        return repository.findByTypeAndCommentId(type, commentId);
    }

    public Optional<Notification> getNotificationByTypeAndPostId(NotificationType type, long postId) {
        return repository.findByTypeAndPostId(type, postId);
    }


}
