package io.github.simulatan.gradle.plugin.buildinfo

import org.eclipse.jgit.api.Git
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

open class BuildInfoTask : DefaultTask() {

	companion object {
		const val NAME = "buildInfo"
	}

	private var extension: BuildInfoExtension? = null
	private var gitInfo: GitInfo? = null

	init {
		extension = project.extensions.findByType(BuildInfoExtension::class.java)!!

		// Should detect...
		//   changes of git commit ids: input=commit-id, output=file
		//   changes of existence of .git directory: input=existence-flag, output=file
		// Should ignore...
		//   changes when git status is dirty: input=commit-id, output=file
		gitInfo = readGitInfo()
		inputs.property("valid", gitInfo!!.valid)
		inputs.property("commit", gitInfo!!.commit)
	}

	@TaskAction
	fun exec() {
		validate()
		mergeManifest()
	}

	private fun mergeManifest() {
		if (!shouldMergeManifest()) {
			logger.debug("Not merging manifest.")
			return
		}

		val attributes = mutableMapOf<String, Any>()

		extension!!.apply {
			if (gitInfo!!.missing && extension!!.warnIfGitDirectoryIsMissing) {
				logger.warn("Could not read .git directory. Git info will not be included in the manifest or will be replaced to invalid values.")
			}
			if (gitInfo!!.valid) {
				if (attributeGitBranchEnabled) {
					attributes["Git-Branch"] = gitInfo!!.branch
				}
				if (attributeGitCommitEnabled) {
					attributes["Git-Commit"] = gitInfo!!.commit
				}
				if (attributeGitCommitterDateEnabled) {
					attributes["Git-Committer-Date"] = gitInfo!!.committerDate
				}
			}
			if (attributeBuildDateEnabled) {
				attributes["Build-Date"] = LocalDateTime.now().atZone(ZoneId.systemDefault())
					.format(DateTimeFormatter.ofPattern(buildDateFormat))
			}
			if (attributeBuildJavaVersionEnabled) {
				attributes["Build-Java-Version"] = System.getProperty("java.version")
			}
			if (attributeBuildJavaVendorEnabled) {
				attributes["Build-Java-Vendor"] = System.getProperty("java.vendor")
			}
			if (attributeBuildOsNameEnabled) {
				attributes["Build-Os-Name"] = System.getProperty("os.name")
			}
			if (attributeBuildOsVersionEnabled) {
				attributes["Build-Os-Version"] = System.getProperty("os.version")
			}
		}

		project.tasks.withType(Jar::class.java).all { jar ->
			jar.manifest {
				it.attributes(attributes)
			}
		}
	}

	private fun readGitInfo(): GitInfo {
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
			committerDate = SimpleDateFormat(extension!!.committerDateFormat).format(Date(commit.commitTime.toLong() * 1000))
		} catch (e: Exception) {
			logger.warn("Couldn't read git repo: {}", e.message)
			missing = true
			// When MODE_IGNORE is used, we skip outputting related info
			valid = extension!!.gitInfoMode != BuildInfoExtension.MODE_IGNORE
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

	private fun validate() {
		// If all options are disabled, skip this task not to cache the result
		if (!shouldMergeManifest()) {
			throw StopExecutionException()
		}

		if (gitInfo!!.missing && extension!!.gitInfoMode == BuildInfoExtension.MODE_ERROR) {
			throw GradleException("Cannot read .git directory.")
		}
	}

	private fun shouldMergeManifest(): Boolean {
		return project.plugins.hasPlugin(JavaPlugin::class.java) && (extension?.manifestEnabled ?: false)
	}
}
