group = "com.coffee-shop"
version = "0.0.1-SNAPSHOT"
description = "coffee-shop"
val charset = "UTF-8"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ben.manes.versions)
    alias(libs.plugins.version.catalog.update)
}


repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spring.io/release")
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.configuration.processor)
    // OpenAPI
    implementation(libs.springdoc.openapi)
    // Database
    runtimeOnly(libs.postgresql)
    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
    // Test
    testImplementation(libs.spring.boot.test)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}


publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = charset
}

tasks.withType<Javadoc> {
    options.encoding = charset
}
