package features.zapret.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.io.File
import java.util.zip.ZipInputStream

import core.data.ConfigLoader
import core.data.PathProvider
import core.data.ProcessExecutor
import features.zapret.domain.ZapretCommand
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.*
import java.io.FileOutputStream

class ZapretRepositoryImpl(
  private val paths: PathProvider,
  private val config: ConfigLoader,
  private val client: HttpClient
) {
  private val executor = ProcessExecutor()

  suspend fun getDownloadUrl(): String {
    val response: JsonObject = client.get(config.zapretGithubRepository).body()

    return response["assets"]?.jsonArray
      ?.firstOrNull { it.jsonObject["name"]?.jsonPrimitive?.content?.endsWith(".zip") == true }
      ?.jsonObject?.get("browser_download_url")?.jsonPrimitive?.content
      ?: throw Exception("ZIP не найден в релизе")
  }

  suspend fun download(url: String) {
    val bytes: ByteArray = client.get(url).body()
    val tempZip = File(paths.rootDir, "temp.zip")

    paths.createRootFolder()
    tempZip.writeBytes(bytes)

    ZipInputStream(tempZip.inputStream()).use { zis ->
      var entry = zis.nextEntry

      while (entry != null) {
        val file = File(paths.rootDir, entry.name)

        if (entry.isDirectory) file.mkdirs()

        else {
          file.parentFile.mkdirs()
          FileOutputStream(file).use { zis.copyTo(it) }
        }

        entry = zis.nextEntry
      }
    }

    tempZip.delete()
  }

  fun runServiceCommand(command: List<String>): Flow<String> {
    return executor.execute(paths.rootDir, "service.bat", command)
  }

  fun removeService(): Flow<String> {
    return runServiceCommand(listOf(ZapretCommand.Remove.id))
  }
}