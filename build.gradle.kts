plugins {
    kotlin("jvm") version libs.versions.kotlin
    `java-library`
}

group = "com.desiderantes.moshi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(libs.versions.java.map { it.toInt() }.get())
}

java {
    toolchain {
        languageVersion.set(libs.versions.java.map(JavaLanguageVersion::of))
    }
    sourceCompatibility = libs.versions.java.map(JavaVersion::toVersion).get()
    targetCompatibility = libs.versions.java.map(JavaVersion::toVersion).get()
    withSourcesJar()
}

dependencies {
    implementation(libs.moshi)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.engine)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
