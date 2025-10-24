package com.ecs.consumer;

import com.ecs.event.LikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class LikeEventConsumer {

    @Bean("like")
    public Consumer<LikeEvent> like() {
        return event -> log.info(event.toString());
    }

}
