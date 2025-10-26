package com.ecs.task;

import com.ecs.client.PostClient;
import com.ecs.domain.LikeNotification;
import com.ecs.domain.Notification;
import com.ecs.domain.Post;
import com.ecs.event.LikeEvent;
import com.ecs.service.NotificationGetService;
import com.ecs.service.NotificationSaveService;
import com.ecs.utils.NotificationIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.ecs.domain.NotificationType.LIKE;


@Slf4j
@Component
public class LikeAddTask {

    private final PostClient postClient;

    private final NotificationGetService getService;

    private final NotificationSaveService saveService;

    public LikeAddTask(PostClient postClient, NotificationGetService getService, NotificationSaveService saveService) {
        this.postClient = postClient;
        this.getService = getService;
        this.saveService = saveService;
    }

    public void processEvent(LikeEvent event) {
        Post post = postClient.getPost(event.getPostId());
        if (post == null) {
            log.error("Post is null with postId:{}", event.getPostId());
            return;
        }

        // 내가 작성한 게시글에 내가 좋아요 누른 경우 알림 무시
        if (post.getUserId() == event.getUserId()) {
            return;
        }
        // likeNotification 신규 생성 또는 업데이트 -> DB 저장
        Notification notification = createOrUpdateNotification(post, event);
        saveService.upsert(notification); // insert 사용하지 않고, upsert 사용 해야 함 (insert 사용시, 동일한 id 가진 알림이 이미 존재할 경우 오류 발생함)
    }

    private Notification createOrUpdateNotification(Post post, LikeEvent event) {
        Optional<Notification> optionalNotification = getService.getNotificationByTypeAndPostId(LIKE, post.getId());

        Instant now = Instant.now();
        Instant retention = now.plus(90, ChronoUnit.DAYS);

        // 기존 알림이 있으면 업데이트, 없으면 새로 생성
        if (optionalNotification.isPresent()) {
            return updateNotification((LikeNotification) optionalNotification.get(), event, now, retention);
        } else {
            return createNotification(post, event, now, retention);
        }
    }

    private Notification updateNotification(LikeNotification notification, LikeEvent event, Instant now, Instant retention) {
        notification.addLiker(event.getUserId(), event.getCreatedAt(), now, retention);
        return notification;
    }

    private Notification createNotification(Post post, LikeEvent event, Instant now, Instant retention) {
        return new LikeNotification(
                NotificationIdGenerator.generate(), post.getUserId(), LIKE, event.getCreatedAt(), now, now, retention, post.getId(), List.of(event.getUserId())
        );
    }
}
