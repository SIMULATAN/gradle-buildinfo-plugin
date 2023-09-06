package io.github.simulatan.gradle.plugin.buildinfo

import io.github.simulatan.gradle.plugin.buildinfo.configuration.BuildInfoExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin

class BuildInfoPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		target.configure(listOf(target)) {
			target.extensions.create(BuildInfoExtension.NAME, BuildInfoExtension::class.java, target)
			val task = target.tasks.register(BuildInfoTask.NAME, BuildInfoTask::class.java)
			if (target.plugins.hasPlugin(JavaPlugin::class.java)) {
				task.configure { it.dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME) }
				getClassesTask(target).dependsOn(BuildInfoTask.NAME)
			} else {
				target.logger.warn("No java plugin found!")
			}
		}
	}

	private fun getClassesTask(project: Project): Task {
		return project.tasks.findByName(JavaPlugin.CLASSES_TASK_NAME)!!
	}
}
