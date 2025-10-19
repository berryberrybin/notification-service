plugins {
    id("java")
    id("org.springframework.boot") version "3.3.2"
    // id("io.spring.dependency-management") version "1.1.6"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// 현재 root 프로젝트는 코드가 없으므로 의존성 주석 처리
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.junit.platform:junit-platform-launcher")
//}
//tasks.test {
//    useJUnitPlatform()
//}

// root 프로젝트와 하위 프로젝트에 공통으로 적용
allprojects {
    group = "com.ecs"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

// 하위 프로젝트에만 적용
subprojects {
    apply {
        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("java-library")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
    tasks.test {
        useJUnitPlatform()
    }
}