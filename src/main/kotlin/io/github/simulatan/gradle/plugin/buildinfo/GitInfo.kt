package io.github.simulatan.gradle.plugin.buildinfo

data class GitInfo(
	val missing: Boolean,
	val valid: Boolean,
	val commit: String,
	val branch: String,
	val committerDate: String
)
