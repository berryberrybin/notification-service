package com.ecs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.ecs", // MongoDB 리포지토리 스캔 위치
        mongoTemplateRef = MongoTemplateConfig.MONGO_TEMPLATE // 사용할 MongoTemplate 빈 이름 지정
)
public class MongoTemplateConfig {

    public static final String MONGO_TEMPLATE = "notificationMongoTemplate";

    @Bean(name = MONGO_TEMPLATE)
    public MongoTemplate notificationMongoTemplate(
            MongoDatabaseFactory notificationMongoFactory,
            MongoConverter mongoConverter) {
        return new MongoTemplate(notificationMongoFactory, mongoConverter);
    }
}

