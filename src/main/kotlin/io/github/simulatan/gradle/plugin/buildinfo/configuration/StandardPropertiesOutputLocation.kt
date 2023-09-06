package io.github.simulatan.gradle.plugin.buildinfo.configuration

object StandardPropertiesOutputLocation {
	val NEWEST = PropertiesOutputLocation { project ->
		listOf(project.buildDir.resolve("newest.buildinfo.properties"))
	}
	val PER_JAR = PropertiesOutputLocation { project ->
		project.tasks.getByName("jar").outputs.files
			.filter { file -> file.extension == "jar" }
			.map { file -> file.resolveSibling("${file.nameWithoutExtension}.buildinfo.properties") }
			.toList()
	}
}
