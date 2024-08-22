package io.github.simulatan.gradle.plugin.buildinfo

import io.github.simulatan.gradle.plugin.buildinfo.configuration.BuildInfoExtension
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

open class BuildInfoTask : DefaultTask() {

	companion object {
		const val NAME = "buildInfo"
	}

	private val extension: BuildInfoExtension = project.extensions.findByType(BuildInfoExtension::class.java)!!
	private val gitInfo: GitInfo = GitInfo.readGitInfo(project, extension)

	init {
		inputs.property("valid", gitInfo.valid)
		inputs.property("commit", gitInfo.commit)

		// never up to date
		outputs.upToDateWhen { false }
		outputs.files(
			extension.propertiesOutputs.flatMap { locationGetter ->
				locationGetter(project)
			}
		)
	}

	@TaskAction
	fun exec() {
		// validate will stop execution if generation is disabled entirely
		// therefore, no further checks are needed below
		validate()

		val attributes = getAttributes()
		writeManifest(attributes)
		writeProperties(attributes)
	}

	/**
	 * Writes the manifest to the classes directory.
	 * This leads to the manifest being included in the jar file.
	 */
	private fun writeManifest(attributes: Map<String, Any>) {
		if (!extension.writeManifest) return

		if (!project.plugins.hasPlugin(JavaPlugin::class.java)) {
			throw GradleException("Java plugin not found! Cannot write manifest.")
		}

		val manifest = project.tasks.findByName(JavaPlugin.JAR_TASK_NAME) as Jar
		manifest.manifest.attributes.putAll(attributes)
	}

	private fun writeProperties(attributes: Map<String, Any>) {
		if (extension.propertiesOutputs.isEmpty()) return

		extension.propertiesOutputs
			.flatMap { locationGetter -> locationGetter(project) }
			.forEach { file ->
				file.parentFile.mkdirs()
				file.createNewFile()
				file.bufferedWriter().use { writer ->
					attributes.forEach { (key, value) ->
						writer.write("$key=$value")
						writer.newLine()
					}
				}
			}
	}

	private fun getAttributes(): Map<String, Any> = extension.run {
		val attributes = extension.extraAttributes

		if (gitInfo.missing && extension.warnIfGitDirectoryIsMissing)
			logger.warn("Could not read .git directory. Git info will not be included in the manifest or will be replaced to invalid values.")

		if (gitInfo.valid) {
			if (attributeGitBranchEnabled) attributes[StandardAttributes.Git.BRANCH] = gitInfo.branch
			if (attributeGitCommitEnabled) attributes[StandardAttributes.Git.COMMIT] = gitInfo.commit
			if (attributeGitCommitterDateEnabled) {
				attributes[StandardAttributes.Git.COMMIT_DATE] = gitInfo.commitDate
				attributes[StandardAttributes.Git.COMMITER_DATE] = gitInfo.commitDate
			}
		}

		if (attributeBuildDateEnabled) attributes[StandardAttributes.Build.DATE] = LocalDateTime.now().atZone(ZoneId.systemDefault())
			.format(DateTimeFormatter.ofPattern(buildDateFormat))
		if (attributeBuildJavaVersionEnabled) attributes[StandardAttributes.Build.JAVA_VERSION] = System.getProperty("java.version")
		if (attributeBuildJavaVendorEnabled) attributes[StandardAttributes.Build.JAVA_VENDOR] = System.getProperty("java.vendor")
		if (attributeBuildOsNameEnabled) attributes[StandardAttributes.Build.OS_NAME] = System.getProperty("os.name")
		if (attributeBuildOsVersionEnabled) attributes[StandardAttributes.Build.OS_VERSION] = System.getProperty("os.version")

		return attributes
	}

	private fun validate() {
		// If the extension is disabled, we don't need to do anything.
		if (!extension.enabled) {
			throw StopExecutionException("Build info generation is disabled.")
		}

		if (gitInfo.missing && extension.gitInfoMode == BuildInfoExtension.MODE_ERROR) {
			throw GradleException("Cannot read .git directory.")
		}
	}
}
