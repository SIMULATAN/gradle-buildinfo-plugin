package io.github.simulatan.gradle.plugin.buildinfo.configuration

import org.gradle.api.Project
import java.io.File

/**
 * Represents a location where the build info properties files should be written to.
 *
 * Takes a project as input and returns a collection of files.
 * The project can be used to react to the project's configuration.
 * @see StandardPropertiesOutputLocation
 */
fun interface PropertiesOutputLocation {
	operator fun invoke(project: Project): Collection<File>
}
