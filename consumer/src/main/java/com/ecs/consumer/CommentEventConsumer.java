package com.ecs.consumer;

import com.ecs.event.CommentEvent;
import com.ecs.task.CommentAddTask;
import com.ecs.task.CommentRemoveTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ecs.event.CommentEventType.ADD;
import static com.ecs.event.CommentEventType.REMOVE;


@Slf4j
@Component
public class CommentEventConsumer {

    private final CommentAddTask commentAddTask;
    private final CommentRemoveTask commentRemoveTask;

    public CommentEventConsumer(CommentAddTask commentAddTask, CommentRemoveTask commentRemoveTask) {
        this.commentAddTask = commentAddTask;
        this.commentRemoveTask = commentRemoveTask;
    }

    @Bean("comment")
    public Consumer<CommentEvent> comment() { // comment 함수명은 spring.cloud.function.definition 프로퍼티와 매칭되어야 함
        return event -> {
            // event 타입에 따라 이벤트를 처리할 적절한 task 호출
            if (event.getType() == ADD) {
                commentAddTask.processEvent(event);
            } else if (event.getType() == REMOVE) {
                commentRemoveTask.processEvent(event);
            }
        };
    }

}
