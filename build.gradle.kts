import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.gradle.plugin-publish") version "1.1.0"
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    `java-library`
}

group = "io.github.simulatan"
version = "2.3.1"

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.sya-ri:kgit:1.0.5")
    implementation(kotlin("gradle-plugin"))
}

gradlePlugin {
    plugins {
        create("buildinfoPlugin") {
            id = "io.github.simulatan.gradle-buildinfo-plugin"
            displayName = "BuildInfo Plugin"
            description = "Bakes in build info at compile time"
            implementationClass = "io.github.simulatan.gradle.plugin.buildinfo.BuildInfoPlugin"
            website.set("https://github.com/SIMULATAN/gradle-buildinfo-plugin")
            vcsUrl.set("https://github.com/SIMULATAN/gradle-buildinfo-plugin.git")
            tags.set(listOf("buildinfo", "metadata", "build"))
        }
    }
}
