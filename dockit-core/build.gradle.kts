plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.github.javaparser:javaparser-core:3.25.4")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}
