package com.ecs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Document("notifications") // MongoDB 컬렉션 이름 지정
public abstract class Notification {
    @Field(targetType = FieldType.STRING) // MongoDB에 저장시 ObjectId('123') 형태로 저장되므로 String 타입으로 매핑
    private String id;
    private Long userId;
    private NotificationType type;
    private Instant occurredAt; // 알림 대상 이벤트 발생 시각 (ex. 댓글 생성 시각)
    private Instant createdAt; // 알림 생성 시각
    private Instant lastUpdatedAt;
    private Instant deletedAt; // 알림 삭제 시각
}
