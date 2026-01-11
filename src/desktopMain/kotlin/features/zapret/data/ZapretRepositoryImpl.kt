package features.zapret.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.File
import java.util.zip.ZipInputStream

import core.data.ConfigLoader
import core.data.PathProvider
import core.data.ProcessExecutor
import features.zapret.domain.ZapretCommand
import features.zapret.domain.model.GithubRelease
import kotlinx.coroutines.flow.Flow

class ZapretRepositoryImpl(
  private val paths: PathProvider,
  private val config: ConfigLoader
) {

  private val executor = ProcessExecutor()
  private val client = HttpClient {
    install(ContentNegotiation) {
      json(Json { ignoreUnknownKeys = true })
    }
  }

  suspend fun getLatestRelease(): GithubRelease {
    return client.get("${config.zapretGithubRepository}/releases/latest").body()
  }

  suspend fun download(url: String): HttpResponse {
    return client.get(url)
  }

  suspend fun extract(response: HttpResponse) {
    val bytes = response.readBytes()

    paths.createRootFolder()

    ZipInputStream(bytes.inputStream()).use { zip ->
      var entry = zip.nextEntry

      while (entry != null) {
        val relativePath = entry.name.substringAfter("/")

        if (relativePath.isNotEmpty()) {
          val file = File(paths.rootDir, relativePath)

          if (entry.isDirectory) {
            file.mkdirs()
          } else {
            file.parentFile.mkdirs()
            file.outputStream().use { zip.copyTo(it) }
          }
        }

        zip.closeEntry()
        entry = zip.nextEntry
      }
    }
  }

  fun runServiceCommand(command: List<String>): Flow<String> {
    return executor.execute(paths.rootDir, "service.bat", command)
  }

  fun removeService(): Flow<String> {
    return runServiceCommand(listOf(ZapretCommand.Remove.id))
  }
}