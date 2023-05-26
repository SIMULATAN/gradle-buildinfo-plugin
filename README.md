# Gradle BuildInfo Plugin
A Gradle plugin to bake in build information into your artifacts. Saves information into `META-INF/MANIFEST.MF` in the built jar file.

## History
This plugin is based on [ksoichiro/gradle-build-info-plugin](https://github.com/ksoichiro/gradle-build-info-plugin), some of the code is copied from there and adapted to kotlin.

The whole plugin was rewritten in Kotlin and the dependencies updated - the original plugin was not maintained anymore since Gradle 2.11!

However, this isn't just an up-to-date version of the original plugin, in the future, this plugin will be extended with additional features.

## Usage
### Groovy
```groovy
plugins {
  id "io.github.simulatan.buildinfo" version "1.0.0"
}
```

### Kotlin
```kotlin
plugins {
  id("io.github.simulatan.buildinfo") version "1.0.0"
}
```

### Tasks
The plugin adds a `buildInfo` task to your project. This task is automatically added as a dependency to the `jar` task.

### Configuration
The plugin adds an extension named `buildInfo` to your project which is used to configure the plugin. The settings available are listed in [BuildInfoExtension.kt](src/main/kotlin/io/github/simulatan/gradle/plugin/buildinfo/BuildInfoExtension.kt).
