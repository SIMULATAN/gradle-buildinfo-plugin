package io.github.simulatan.gradle.plugin.buildinfo

import io.github.simulatan.gradle.plugin.buildinfo.configuration.BuildInfoExtension
import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import java.text.SimpleDateFormat
import java.util.*

data class GitInfo(
	val missing: Boolean,
	val valid: Boolean,
	val commit: String,
	val branch: String,
	val committerDate: String
) {
	companion object {
		private val LOGGER = Logging.getLogger(GitInfo::class.java)

		fun readGitInfo(project: Project, extension: BuildInfoExtension): GitInfo {
			var missing = false
			var valid = true
			var branch: String
			var commitName: String
			var committerDate: String
			try {
				val git = Git.open(project.projectDir)
				branch = git.repository.branch
				val head = git.repository.findRef("HEAD")
				val commit = git.repository.parseCommit(head.objectId)
				commitName = commit.name
				committerDate = SimpleDateFormat(extension.committerDateFormat).format(Date(commit.commitTime.toLong() * 1000))
			} catch (e: Exception) {
				LOGGER.warn("Couldn't read git repo: {}", e.message)
				missing = true
				// When MODE_IGNORE is used, we skip outputting related info
				valid = extension.gitInfoMode != BuildInfoExtension.MODE_IGNORE
				branch = "unknown"
				commitName = "unknown"
				committerDate = "unknown"
			}
			return GitInfo(
				missing = missing,
				valid = valid,
				branch = branch,
				commit = commitName,
				committerDate = committerDate
			)
		}
	}
}
