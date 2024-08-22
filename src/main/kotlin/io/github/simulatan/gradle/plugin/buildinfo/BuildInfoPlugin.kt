package io.github.simulatan.gradle.plugin.buildinfo

import io.github.simulatan.gradle.plugin.buildinfo.configuration.BuildInfoExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

class BuildInfoPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		target.configure(listOf(target)) {
			target.extensions.create(BuildInfoExtension.NAME, BuildInfoExtension::class.java, target)
			val buildInfoTask = target.tasks.register(BuildInfoTask.NAME, BuildInfoTask::class.java) {
				it.group = "build"
				it.description = "Generates build info"
			}

			if (target.plugins.hasPlugin(JavaPlugin::class.java)
				|| target.plugins.hasPlugin(KotlinBasePlugin::class.java)
			) {
				buildInfoTask.configure { it.dependsOn(JavaPlugin.PROCESS_RESOURCES_TASK_NAME) }
				target.tasks.findByName(JavaPlugin.CLASSES_TASK_NAME)!!.dependsOn(BuildInfoTask.NAME)
			} else {
				target.logger.warn("No java or kotlin plugin found!")
			}
		}
	}
}
