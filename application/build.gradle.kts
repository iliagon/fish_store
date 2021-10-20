import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    // Define plugins version in a single place
    extra.apply {
        set("kotlinVersion", "1.5.31")
    }
}

val javaVersion = JavaVersion.VERSION_11
val mapstructVersion = "1.4.2.Final"

plugins {
    val kotlinVersion: String by extra

    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    application
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

sourceSets {
    named("main") {
        java.srcDir("build/generated/openApi/src/main/kotlin")
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    implementation("io.swagger:swagger-annotations:1.6.3")
    implementation("org.openapitools:jackson-databind-nullable:0.2.1")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "ERROR")
    }
}

tasks {
    compileKotlin() {
        //Generate Controllers and Dto based OpenApi Spec
        dependsOn(":openapi_codegen:openApiGenerate")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion.toString()
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
