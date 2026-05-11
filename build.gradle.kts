import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    kotlin("plugin.spring") version "2.3.0"
    kotlin("plugin.jpa") version "2.3.0"

    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"


    // --- Database Migration ---
//    id("org.flywaydb.flyway") version "9.22.0"
}

//flyway {
//    url = "jdbc:mysql://localhost:3306/auth_dev"
//    user = "root"
//    password = "Sentry_Password"
//    schemas = arrayOf("auth_dev")
//    locations = arrayOf("filesystem:src/main/resources/db/migration")
//}

group = "com.xapps"
version = "0.0.1-SNAPSHOT"
description = "Auth Service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    // ---- PROJECT DEPENDENCIES ----
    implementation(project(":contracts"))
    implementation(project(":platform"))
//    implementation(project(":common:file_manager"))

    /* =========================================================
     * CORE SPRING BOOT + WEBFLUX
     * ========================================================= */
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        // Remove Jackson Completely
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.fasterxml.jackson.module")
        exclude(group = "com.fasterxml.jackson.datatype")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    /* =========================================================
     * DATABASE – R2DBC + FLYWAY
     * ========================================================= */
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("io.r2dbc:r2dbc-pool")
//    implementation("org.flywaydb:flyway-core")
//    implementation("org.flywaydb:flyway-mysql")
//    runtimeOnly("io.asyncer:r2dbc-mysql:1.4.1")
//    runtimeOnly("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
//    implementation("mysql:mysql-connector-java:8.0.33")

    /* =========================================================
     * EXPOSED ORM
     * ========================================================= */
    val exposedVersion = "1.0.0-rc-2"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-r2dbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")

    /* =========================================================
     * REACTIVE + KOTLIN
     * ========================================================= */
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

    /* =========================================================
     * SERIALIZATION & REFLECTION
     * ========================================================= */
    val kotlinSerialization = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinSerialization")
    implementation("org.jetbrains.kotlin:kotlin-reflect")


    // Ktor
    val ktor = "3.3.3" // formerly 2.3.12
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-network:${ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")

    // OkHttp
    val okHttpVersion = "5.3.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    /* =========================================================
     * UTILITIES
     * ========================================================= */
    implementation("com.benasher44:uuid:0.8.4")
    implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")

    /* =========================================================
     * DEV / LOMBOK
     * ========================================================= */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    /* =========================================================
     * TESTING
     * ========================================================= */
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    /* =========================================================
     * INTERNAL MODULES
     * ========================================================= */


    // Mongo DB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
//    implementation("org.mongodb:mongodb-driver-kotlin-coroutine")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property"
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
