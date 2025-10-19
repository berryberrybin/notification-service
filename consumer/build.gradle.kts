dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
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