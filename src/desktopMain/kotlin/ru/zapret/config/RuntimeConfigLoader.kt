package ru.zapret.config

import java.util.Properties

object RuntimeConfigLoader {
  fun load(): ZapretRuntimeConfig {
    val props = Properties()

    RuntimeConfigLoader::class.java.classLoader
      .getResourceAsStream("application.properties")
      ?.use { props.load(it) }
      ?: error("application.properties not found")

    return ZapretRuntimeConfig(
      processName = props.getProperty("zapret.process.name"),
      installDirName = props.getProperty("zapret.install.dir"),
      serviceBatName = props.getProperty("zapret.service.bat"),
      githubDownloadUrl = props.getProperty("github.api.url")
    )
  }
}