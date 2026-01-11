package ru.zapret.config

data class ZapretRuntimeConfig(
  val processName: String,
  val installDirName: String,
  val serviceBatName: String,
  val githubDownloadUrl: String
)