@file:Suppress("UnstableApiUsage")

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
    apply(plugin = "kotlin")
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // Logging
        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("ch.qos.logback:logback-classic:1.4.7")

        // Testing
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
        testImplementation("io.mockk:mockk:1.13.8")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
