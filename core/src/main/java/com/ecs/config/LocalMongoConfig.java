package com.ecs.config;

// MongoDB 인스턴스를 생성, 실행, 삭제, 중지 수행
// mongoDB 팩토리 생성 - MongoDB 연결 설정 역할 수행

import com.mongodb.ConnectionString;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Configuration
public class LocalMongoConfig {

    private static final String MONGODB_IMAGE_NAME = "mongo:5.0"; // 도커 이미지 이름
    private static final int MONGODB_INNER_PORT = 27017; // 도커 컨테이너 내부 포트 (MongoDB 기본 포트는 27017로 고정됨)
    private static final String DATABASE_NAME = "notification";
    private static final GenericContainer mongoInstance = createMongoInstance();

    // testContainer에서 제공하는 GenericContainer 클래스를 사용하여 MongoDB 도커 컨테이너 생성
    private static GenericContainer createMongoInstance() {
        return new GenericContainer(DockerImageName.parse(MONGODB_IMAGE_NAME)) // 도커 이미지 이름 설정
                .withExposedPorts(MONGODB_INNER_PORT) // 외부에 노출할 포트 설정
                .withReuse(true); // 컨테이너 재사용 설정 (테스트시 매번 컨테이너를 새로 생성하지 않도록)
    }

    @PostConstruct // 빈 초기화 후 실행되는 메서드 (스프링 시작시 1번 실행됨)
    public void startMongo() {
        try {
            mongoInstance.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy // 빈 소멸 전 실행되는 메서드 (종료시 1번 실행됨)
    public void stopMongo() {
        try {
            if (mongoInstance.isRunning()) mongoInstance.stop();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    // MongoDatabaseFactory 생성
    @Bean(name = "notificationMongoFactory")
    public MongoDatabaseFactory notificationMongoFactory() {
        return new SimpleMongoClientDatabaseFactory(connectionString());
    }

    /*
     * mongoInstance.getMappedPort(MONGODB_INNER_PORT) : 도커 컨테이너의 내부 포트를 외부에 매핑된 포트로 변환
     * (내부에서는 27017 포트 사용하지만 외부에서 접근시 임의로 할당된 포트 사용해야 함)
     */
    private ConnectionString connectionString() {
        String host = mongoInstance.getHost();
        Integer port = mongoInstance.getMappedPort(MONGODB_INNER_PORT);
        return new ConnectionString("mongodb://" + host + ":" + port + "/" + DATABASE_NAME);
    }
}