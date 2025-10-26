package com.ecs.domain;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;

@Getter
@TypeAlias("CommentNotification") // NoSQL에서 객체 타입 정보를 자동으로 저장하여 역직렬화할 때 사용
public class CommentNotification extends Notification {
    private final long postId; // 댓글이 작성된 게시물 ID
    private final long writerId;
    private final String comment;
    private final long commentId;

    public CommentNotification(String id, long userId, NotificationType type,
                               Instant occurredAt, Instant createdAt, Instant lastUpdatedAt, Instant deletedAt,
                               long postId, long writerId, String comment, long commentId) {
        super(id, userId, type, occurredAt, createdAt, lastUpdatedAt, deletedAt);
        this.postId = postId;
        this.writerId = writerId;
        this.comment = comment;
        this.commentId = commentId;
    }
}
