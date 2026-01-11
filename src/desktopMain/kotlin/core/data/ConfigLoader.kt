package core.data

import java.util.Properties

class ConfigLoader {
  private val props = Properties()

  init {
    val inputStream = javaClass.classLoader.getResourceAsStream("application.properties")

    if (inputStream != null) {
      props.load(inputStream)
    }
  }

  fun getProperty(key: String, default: String): String {
    return props.getProperty(key, default)
  }

  val zapretFolderName: String
    get() = getProperty("zapret.folder.name", "zapret-discord-youtube")

  val zapretGithubRepository: String
    get() = getProperty("zapret.github.repository", "https://api.github.com/repos/Flowseal/zapret-discord-youtube/releases/latest")
}
