package com.ecs.task;

import com.ecs.domain.LikeNotification;
import com.ecs.domain.Notification;
import com.ecs.event.LikeEvent;
import com.ecs.service.NotificationGetService;
import com.ecs.service.NotificationRemoveService;
import com.ecs.service.NotificationSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

import static com.ecs.domain.NotificationType.LIKE;

@Slf4j
@Component
public class LikeRemoveTask {

    private final NotificationGetService getService;

    private final NotificationRemoveService removeService;

    private final NotificationSaveService saveService;

    public LikeRemoveTask(NotificationGetService getService, NotificationRemoveService removeService, NotificationSaveService saveService) {
        this.getService = getService;
        this.removeService = removeService;
        this.saveService = saveService;
    }

    public void processEvent(LikeEvent event) {
        Optional<Notification> optionalNotification = getService.getNotificationByTypeAndPostId(LIKE, event.getPostId());
        if (optionalNotification.isEmpty()) {
            log.error("No notification with postId: {}", event.getPostId());
            return;
        }

        // likers 에서 event.userId 제거
        // likers 가 비어있으면 알림 삭제, 아니면 알림 업데이트
        LikeNotification notification = (LikeNotification) optionalNotification.get();
        removeLikerAndUpdateNotification(notification, event);
    }

    private void removeLikerAndUpdateNotification(LikeNotification notification, LikeEvent event) {
        notification.removeLiker(event.getUserId(), Instant.now());

        if (notification.getLikerIds().isEmpty()) {
            removeService.deleteById(notification.getId()); // likers 가 비어있으면 알림 삭제 (좋아요한 사람이 아무도 없는데 해당 알림 데이터를 유지할 필요가 없음)
        } else {
            saveService.upsert(notification); // likers 가 비어있지 않으면 알림 업데이트
        }
    }
}
