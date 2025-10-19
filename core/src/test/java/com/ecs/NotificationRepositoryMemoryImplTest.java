package com.ecs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// 통합 테스트로 변경 (testcontainers 통해 MongoDB 도커 컨테이너 사용)
@SpringBootApplication
@SpringBootTest
class NotificationRepositoryMemoryImplTest {

    // private final NotificationRepositoryMemoryImpl sut = new NotificationRepositoryMemoryImpl();
    // memeory DB 대신 실제 MongoDB를 사용하는 테스트로 변경
    @Autowired
    private NotificationRepository sut;

    private final Instant now = Instant.now();
    private final Instant deletedAt = now.plus(90, ChronoUnit.DAYS);

    @Test
    void save() {
        sut.save(new Notification("1", 100L, NotificationType.LIKE, now, deletedAt));
        Optional<Notification> notification = sut.findById("1");
        assertTrue(notification.isPresent());
    }

    /*
    mongoDB는 밀리초 이하 단위를 저장하지 않음 때문에 아래 테스트에서 getEpochSecond()로 비교하도록 수정
        - Expected :2025-10-19T13:38:33.694Z
        - Actual   :2025-10-19T13:38:33.694474Z
     */
    @Test
    void findById() {
        sut.save(new Notification("2", 200L, NotificationType.LIKE, now, deletedAt));
        Notification notification = sut.findById("2").orElseThrow();
        assertEquals(notification.id, "2");
        assertEquals(notification.userId, 200L);
        assertEquals(notification.type, NotificationType.LIKE);
        // assertEquals(notification.createdAt, now);
        assertEquals(notification.createdAt.getEpochSecond(), now.getEpochSecond());
        assertEquals(notification.deletedAt.getEpochSecond(), deletedAt.getEpochSecond());
    }

    @Test
    void deleteById() {
        sut.save(new Notification("3", 300L, NotificationType.LIKE, now, deletedAt));
        sut.deleteById("3");

        Optional<Notification> notification = sut.findById("3");
        assertFalse(notification.isPresent());
    }
}