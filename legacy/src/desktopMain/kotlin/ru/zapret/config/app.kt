package ru.zapret.config

import java.util.Properties

object AppConfig {
  private val properties = Properties()

  lateinit var githubApiUrl: String
  lateinit var appDirName: String
  lateinit var consoleEncoding: String

  var maxLogLines = 300

  fun load() {
    val stream = AppConfig::class.java.classLoader.getResourceAsStream("application.properties")

    if (stream != null) {
      properties.load(stream)

      githubApiUrl = properties.getProperty("github.api.url")
      appDirName = properties.getProperty("app.dir.name")
      consoleEncoding = properties.getProperty("console.encoding")
      maxLogLines = properties.getProperty("app.logs.max_lines").toInt()
    }
  }
}
