package com.ecs.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;
import java.util.List;

@Getter
@TypeAlias("LikeNotification")
public class LikeNotification extends Notification {
    private final long postId;
    private final List<Long> likerIds;

    public LikeNotification(String id, long userId, NotificationType type, Instant occurredAt, Instant createdAt,
                            Instant lastUpdatedAt, Instant deletedAt, long postId, List<Long> likerIds) {
        super(id, userId, type, occurredAt, createdAt, lastUpdatedAt, deletedAt);
        this.postId = postId;
        this.likerIds = likerIds;
    }

    public void addLiker(long likerId, Instant occurredAt, Instant now, Instant retention) {
        this.likerIds.add(likerId);
        this.setOccurredAt(occurredAt);
        this.setLastUpdatedAt(now);
        this.setDeletedAt(retention);
    }

    // removeLiker 할 때 occuredAt은 변경하지 않음 -> 알림 리스트 조회시 occurredAt 기준으로 정렬하기 때문에 변경시 순서가 바뀔 수 있음
    // deleteAt도 변경하지 않음 -> 좋아요가 모두 취소되었을 때 내일 삭제될 알림을 다시 일정 기간 더 보관 후 삭제할 필요가 없음
    public void removeLiker(long userId, Instant now) {
        this.likerIds.remove(userId);
        this.setLastUpdatedAt(now);
    }
}
