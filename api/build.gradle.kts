dependencies {
    // 멀티 모듈 구성: core 프로젝트 의존성 추가
    implementation(project(mapOf("path" to ":core")))
    // 스프링 웹 스타터 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springdoc:springdoc-openapi-starter-common:2.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
}
