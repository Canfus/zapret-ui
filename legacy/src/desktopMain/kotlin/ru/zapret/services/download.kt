package ru.zapret.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

class DownloadService(private val client: HttpClient) {
  suspend fun getDownloadUrl(apiUrl: String): String {
    val response: JsonObject = client.get(apiUrl).body()

    return response["assets"]?.jsonArray
      ?.firstOrNull { it.jsonObject["name"]?.jsonPrimitive?.content?.endsWith(".zip") == true }
      ?.jsonObject?.get("browser_download_url")?.jsonPrimitive?.content
      ?: throw Exception("ZIP не найден в релизе")
  }

  suspend fun download(url: String, targetDir: File, onLog: (String) -> Unit) {
    onLog("Скачивание свежей версии zapret...")

    val bytes: ByteArray = client.get(url).body()
    val tempZip = File(targetDir, "temp.zip")

    targetDir.mkdirs()
    tempZip.writeBytes(bytes)

    onLog("Распаковка...")

    ZipInputStream(tempZip.inputStream()).use { zis ->
      var entry = zis.nextEntry

      while (entry != null) {
        val file = File(targetDir, entry.name)

        if (entry.isDirectory) file.mkdirs()

        else {
          file.parentFile.mkdirs()
          FileOutputStream(file).use { zis.copyTo(it) }
        }

        entry = zis.nextEntry
      }

      tempZip.delete()
      onLog("Установка zapret успешно завершена")
    }
  }
}
