package com.ecs.task;

import com.ecs.client.PostClient;
import com.ecs.domain.Post;
import com.ecs.event.CommentEvent;
import com.ecs.service.NotificationGetService;
import com.ecs.service.NotificationRemoveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.ecs.domain.NotificationType.COMMENT;


@Slf4j
@Component
public class CommentRemoveTask {

    private final PostClient postClient;

    private final NotificationGetService getService;

    private final NotificationRemoveService removeService;

    public CommentRemoveTask(PostClient postClient, NotificationGetService getService, NotificationRemoveService removeService) {
        this.postClient = postClient;
        this.getService = getService;
        this.removeService = removeService;
    }

    public void processEvent(CommentEvent event) {
        // 내가 작성한 댓글인 경우 댓글 삭제 알림 무시
        Post post = postClient.getPost(event.getPostId());
        if (Objects.equals(post.getUserId(), event.getUserId())) {
            return;
        }

        // 댓글 삭제되면 알림도 삭제
        getService.getNotificationByTypeAndCommentId(COMMENT, event.getCommentId())
                .ifPresentOrElse(
                        notification -> removeService.deleteById(notification.getId())
                        , () -> log.error("notification not found for deleted comment: {}", event.getCommentId())
                );
    }
}
