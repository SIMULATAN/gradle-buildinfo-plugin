plugins {
    id("com.gradle.plugin-publish") version "1.1.0"
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    `java-library`
}

group = "io.github.simulatan"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.sya-ri:kgit:1.0.5")
}

gradlePlugin {
    plugins {
        create("buildinfoPlugin") {
            id = "io.github.simulatan.gradle-buildinfo-plugin"
            displayName = "BuildInfo Plugin"
            description = "Bakes in build info at compile time"
            implementationClass = "io.github.simulatan.gradle.plugin.buildinfo.BuildInfoPlugin"
        }
    }
}
