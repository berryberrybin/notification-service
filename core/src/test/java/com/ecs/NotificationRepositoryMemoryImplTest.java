package com.ecs;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryMemoryImplTest {

    private final NotificationRepositoryMemoryImpl sut = new NotificationRepositoryMemoryImpl();

    private final Instant now = Instant.now();
    private final Instant deletedAt = now.plus(90, ChronoUnit.DAYS);

    @Test
    void save() {
        sut.save(new Notification("1", 100L, NotificationType.LIKE, now, deletedAt));
        Optional<Notification> notification = sut.findById("1");
        assertTrue(notification.isPresent());
    }

    @Test
    void findById() {
        sut.save(new Notification("2", 200L, NotificationType.LIKE, now, deletedAt));
        Notification notification = sut.findById("2").orElseThrow();
        assertEquals(notification.id, "2");
        assertEquals(notification.userId, 200L);
        assertEquals(notification.type, NotificationType.LIKE);
        assertEquals(notification.createdAt, now);
        assertEquals(notification.deletedAt, deletedAt);
    }

    @Test
    void deleteById() {
        sut.save(new Notification("3", 300L, NotificationType.LIKE, now, deletedAt));
        sut.deleteById("3");

        Optional<Notification> notification = sut.findById("3");
        assertFalse(notification.isPresent());
    }
}