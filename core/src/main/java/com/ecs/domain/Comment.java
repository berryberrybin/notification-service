package com.ecs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Comment {
    private long id;
    private long userId;
    private String content;
    private Instant createdAt;
}
