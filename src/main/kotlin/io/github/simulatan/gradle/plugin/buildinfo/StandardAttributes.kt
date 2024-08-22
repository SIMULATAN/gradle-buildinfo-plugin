package io.github.simulatan.gradle.plugin.buildinfo

object StandardAttributes {
	object Git {
		const val BRANCH = "Git-Branch"
		const val COMMIT = "Git-Commit"
		@Deprecated("Use COMMIT_DATE instead", ReplaceWith("COMMIT_DATE"))
		const val COMMITER_DATE = "Git-Committer-Date"
		const val COMMIT_DATE = "Git-Commit-Date"
	}

	object Build {
		const val DATE = "Build-Date"
		const val JAVA_VERSION = "Build-Java-Version"
		const val JAVA_VENDOR = "Build-Java-Vendor"
		const val OS_NAME = "Build-Os-Name"
		const val OS_VERSION = "Build-Os-Version"
	}
}
