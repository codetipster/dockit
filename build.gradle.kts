@file:Suppress("UnstableApiUsage")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.toolchain.JvmVendorSpec

plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("java")
    id("idea")
}

allprojects {
    group = "com.dockit"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))

        }
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    dependencies {
        implementation("com.github.javaparser:javaparser-core:3.25.4")
        implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.20")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("ch.qos.logback:logback-classic:1.4.7")

        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
        testImplementation("io.mockk:mockk:1.13.8")
    }

    tasks.test {
        useJUnitPlatform()
    }

    configure<SourceSetContainer> {
        named("main") {
            java.srcDir("src/main/java")
            java.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
        named("test") {
            java.srcDir("src/test/java")
            java.srcDir("src/test/kotlin")
            resources.srcDir("src/test/resources")
        }
    }
}