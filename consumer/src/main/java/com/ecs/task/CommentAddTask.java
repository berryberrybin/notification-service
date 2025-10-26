package com.ecs.task;


import com.ecs.client.CommentClient;
import com.ecs.client.PostClient;
import com.ecs.domain.*;
import com.ecs.event.CommentEvent;
import com.ecs.service.NotificationSaveService;
import com.ecs.utils.NotificationIdGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class CommentAddTask {

    private final PostClient postClient;

    private final CommentClient commentClient;

    private final NotificationSaveService saveService;

    public CommentAddTask(PostClient postClient, CommentClient commentClient, NotificationSaveService saveService) {
        this.postClient = postClient;
        this.commentClient = commentClient;
        this.saveService = saveService;
    }

    public void processEvent(CommentEvent event) {
        // 내가 작성한 댓글인 경우 무시
        Post post = postClient.getPost(event.getPostId());
        if (Objects.equals(post.getUserId(), event.getUserId())) {
            return;
        }

        // 알림 생성
        Comment comment = commentClient.getComment(event.getCommentId());
        Notification notification = createNotification(post, comment);

        // 저장
        saveService.insert(notification);
    }

    private Notification createNotification(Post post, Comment comment) {
        Instant now = Instant.now();

        return new CommentNotification(
                NotificationIdGenerator.generate(),
                post.getUserId(),
                NotificationType.COMMENT,
                comment.getCreatedAt(),
                now,
                now,
                now.plus(90, ChronoUnit.DAYS),
                post.getId(),
                comment.getUserId(),
                comment.getContent(),
                comment.getId()
        );
    }
}
