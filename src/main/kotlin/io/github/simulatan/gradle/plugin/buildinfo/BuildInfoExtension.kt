package io.github.simulatan.gradle.plugin.buildinfo

import org.gradle.api.Project

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

	var committerDateFormat: String = DEFAULT_DATE_FORMAT
	var buildDateFormat: String = DEFAULT_DATE_FORMAT
	var manifestEnabled: Boolean = true
	var gitInfoMode: Int = MODE_DEFAULT
	var warnIfGitDirectoryIsMissing: Boolean = true
	var attributeGitBranchEnabled: Boolean = true
	var attributeGitCommitEnabled: Boolean = true
	var attributeGitCommitterDateEnabled: Boolean = true
	var attributeBuildDateEnabled: Boolean = true
	var attributeBuildJavaVersionEnabled: Boolean = true
	var attributeBuildJavaVendorEnabled: Boolean = true
	var attributeBuildOsNameEnabled: Boolean = true
	var attributeBuildOsVersionEnabled: Boolean = true
}
