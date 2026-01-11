package ru.zapret.infrastructure.download

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

interface FileDownloader {
  suspend fun download(url: String, target: File): File
}

class HttpFileDownloader(
  private val httpClient: HttpClient
) : FileDownloader {
  override suspend fun download(url: String, target: File): File =
    withContext(Dispatchers.IO) {
        val channel: ByteReadChannel = httpClient
          .get(url)
          .bodyAsChannel()

      target.outputStream().use { output ->
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        while (!channel.isClosedForRead) {
          val read = channel.readAvailable(buffer)

          if (read == -1) break

          output.write(buffer, 0, read)
        }
      }
      
      target
    }
}