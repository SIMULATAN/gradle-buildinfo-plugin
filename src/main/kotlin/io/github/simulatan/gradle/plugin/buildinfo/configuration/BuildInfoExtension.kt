package io.github.simulatan.gradle.plugin.buildinfo.configuration

import org.gradle.api.Project
import java.io.File

open class BuildInfoExtension(project: Project) {

	companion object {
		const val NAME = "buildInfo"
		const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

		/**
		 * When the plugin cannot read .git directory,
		 * set the values of branch, commit, and committer date to "unknown",
		 * then proceed task.
		 */
		const val MODE_DEFAULT = 0

		/**
		 * When the plugin cannot read .git directory,
		 * ignore it and proceed task.
		 */
		const val MODE_IGNORE = 1

		/**
		 * When the plugin cannot read .git directory,
		 * throw an exception to stop build.
		 */
		const val MODE_ERROR = 2
	}

	var enabled: Boolean = true
	/**
	 * Whether to write the properties as a manifest to the classes directory.
	 * This leads to the manifest being included in the jar file.
	 */
	var writeManifest = true
	/**
	 * Specifies where to write the properties to.
	 * Set to an empty list to disable properties output.
	 */
	var propertiesOutputs = emptyList<PropertiesOutputLocation>()
	var committerDateFormat: String = DEFAULT_DATE_FORMAT
	var buildDateFormat: String = DEFAULT_DATE_FORMAT
	var gitInfoMode: Int = MODE_DEFAULT
	var warnIfGitDirectoryIsMissing: Boolean = true
	var gitDirectory: File = project.projectDir.resolve(".git")
	var attributeGitBranchEnabled: Boolean = true
	var attributeGitCommitEnabled: Boolean = true
	var attributeGitCommitterDateEnabled: Boolean = true
	var attributeBuildDateEnabled: Boolean = true
	var attributeBuildJavaVersionEnabled: Boolean = true
	var attributeBuildJavaVendorEnabled: Boolean = true
	var attributeBuildOsNameEnabled: Boolean = true
	var attributeBuildOsVersionEnabled: Boolean = true

	val extraAttributes = mutableMapOf<String, Any>()

	fun extraAttribute(key: String, value: Any) {
		this.extraAttributes[key] = value
	}

	fun extraAttributes(attributes: Map<String, Any>) {
		this.extraAttributes.putAll(attributes)
	}

	fun extraAttributes(vararg attributes: Pair<String, Any>) {
		this.extraAttributes.putAll(attributes)
	}
}
