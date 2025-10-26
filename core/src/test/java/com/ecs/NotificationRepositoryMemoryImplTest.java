package com.ecs;

import com.ecs.domain.CommentNotification;
import com.ecs.domain.Notification;
import com.ecs.repository.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static com.ecs.domain.NotificationType.COMMENT;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;

// 통합 테스트로 변경 (testcontainers 통해 MongoDB 도커 컨테이너 사용)
@SpringBootApplication
@SpringBootTest
class NotificationRepositoryMemoryImplTest {

    // private final NotificationRepositoryMemoryImpl sut = new NotificationRepositoryMemoryImpl();
    // memeory DB 대신 실제 MongoDB를 사용하는 테스트로 변경
    @Autowired
    private NotificationRepository sut;

    private final long userId = 2L;
    private final long postId = 3L;
    private final long writerId = 4L;
    private final long commentId = 5L;
    private final String comment = "comment";
    private final Instant now = Instant.now();
    private final Instant ninetyDaysAfter = Instant.now().plus(90, DAYS);

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 5; i++) {
            Instant occurredAt = now.minus(i, DAYS);
            sut.save(new CommentNotification("id-" + i, userId, COMMENT, occurredAt, now, now, ninetyDaysAfter,
                    postId, writerId, comment, commentId));
        }
    }

    @AfterEach
    void tearDown() {
        sut.deleteAll();
    }

    @Test
    void save() {
        String id = "1";
        sut.save(createCommentNotification(id));
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

        String id = "2";
        sut.save(createCommentNotification(id));
        CommentNotification notification = (CommentNotification) sut.findById("2").orElseThrow();
        assertEquals(notification.getId(), id);
        assertEquals(notification.getUserId(), userId);
        assertEquals(notification.getOccurredAt().getEpochSecond(), now.getEpochSecond());
        assertEquals(notification.getCreatedAt().getEpochSecond(), now.getEpochSecond());
        assertEquals(notification.getLastUpdatedAt().getEpochSecond(), now.getEpochSecond());
        assertEquals(notification.getDeletedAt().getEpochSecond(), ninetyDaysAfter.getEpochSecond());
        assertEquals(notification.getPostId(), postId);
        assertEquals(notification.getWriterId(), writerId);
        assertEquals(notification.getComment(), comment);
        assertEquals(notification.getCommentId(), commentId);
    }

    @Test
    void deleteById() {
        String id = "3";

        sut.save(createCommentNotification(id));
        sut.deleteById(id);
        Optional<Notification> optionalNotification = sut.findById(id);

        assertFalse(optionalNotification.isPresent());
    }

    private CommentNotification createCommentNotification(String id) {
        return new CommentNotification(id, userId, COMMENT, now, now, now, ninetyDaysAfter, postId, writerId, comment,
                commentId);
    }
}