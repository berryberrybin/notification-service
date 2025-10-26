package com.ecs.domain;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;

@Getter
@TypeAlias("FollowNotification")
public class FollowNotification extends Notification {

    private final long followerId; // 팔로워의 사용자 ID (팔로워 신청한 사람)

    public FollowNotification(String id, long userId, NotificationType type, Instant occurredAt, Instant createdAt,
                              Instant lastUpdatedAt, Instant deletedAt, long followerId) {
        super(id, userId, type, occurredAt, createdAt, lastUpdatedAt, deletedAt);
        this.followerId = followerId;
    }
}
