dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // testcontainer는 테스트 뿐만 아니라 일반 스프링부트 띄웠을 때도 활용 예정이므로 testimplementation으로 선언하지 않았음
    implementation("org.testcontainers:testcontainers:1.20.1")
    api("org.springframework.cloud:spring-cloud-starter-stream-kafka")
}