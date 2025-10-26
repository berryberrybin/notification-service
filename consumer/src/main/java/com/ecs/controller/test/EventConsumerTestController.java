package com.ecs.controller.test;

import com.ecs.event.CommentEvent;
import com.ecs.event.FollowEvent;
import com.ecs.event.LikeEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

// Spring Cloud Function에서 등록한 Consumer<CommentEvent> 함수를 직접 호출하는 테스트용 REST 컨트롤러
@RestController
public class EventConsumerTestController implements EventConsumerTestControllerSpec{

    private final Consumer<CommentEvent> commentEventConsumer;
    private final Consumer<LikeEvent> likeEventConsumer;
    private final Consumer<FollowEvent> followEventConsumer;

    public EventConsumerTestController(Consumer<CommentEvent> commentEventConsumer, Consumer<LikeEvent> likeEventConsumer, Consumer<FollowEvent> follow) {
        this.commentEventConsumer = commentEventConsumer;
        this.likeEventConsumer = likeEventConsumer;
        this.followEventConsumer = follow;
    }

    @Override
    @PostMapping("/test/comment")
    public void comment(@RequestBody CommentEvent commentEvent) {
        commentEventConsumer.accept(commentEvent);
    }

    @Override
    @PostMapping("/test/like")
    public void like(@RequestBody LikeEvent event) { likeEventConsumer.accept(event); }

    @Override
    @PostMapping("/test/follow")
    public void follow(@RequestBody FollowEvent event) { followEventConsumer.accept(event); }
}
