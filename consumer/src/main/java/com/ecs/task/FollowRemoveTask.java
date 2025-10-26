package com.ecs.task;

import com.ecs.domain.NotificationType;
import com.ecs.event.FollowEvent;
import com.ecs.service.NotificationGetService;
import com.ecs.service.NotificationRemoveService;
import org.springframework.stereotype.Component;

@Component
public class FollowRemoveTask {

    private final NotificationGetService getService;

    private final NotificationRemoveService removeService;

    public FollowRemoveTask(NotificationGetService getService, NotificationRemoveService removeService) {
        this.getService = getService;
        this.removeService = removeService;
    }

    public void processEvent(FollowEvent event) {
        getService.getNotificationByTypeAndUserIdAndFollowerId(NotificationType.FOLLOW, event.getTargetUserId(), event.getUserId())
                .ifPresent(notification -> removeService.deleteById(notification.getId()));
    }
}
