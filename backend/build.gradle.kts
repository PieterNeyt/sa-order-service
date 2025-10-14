plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.github.node-gradle.node") version "7.1.0"
}

group = "be.kdg.sa"
version = "0.0.1-SNAPSHOT"
description="backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.jmolecules.integrations:jmolecules-starter-ddd:0.29.0")
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.boot:spring-boot-starter-web")


    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.jmolecules.integrations:jmolecules-starter-test:0.29.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}



tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}

val webAppDir="./src/main/resources/static"


tasks.register<Copy>("installWebApp") {
    group = "build"
    description = "Installs the web app in resources/static"
    dependsOn(":frontend:build")
    from("../frontend/dist")
    into( webAppDir)
}

tasks.register<Delete>("cleanWebApp") {
    group = "build"
    description = "Cleans the web app from resources/static"
    delete(webAppDir)
}

tasks.processResources {
    dependsOn("installWebApp")
}

tasks.clean {
    dependsOn("cleanWebApp")
}

