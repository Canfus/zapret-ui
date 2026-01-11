package ru.zapret.infrastructure.archive

import java.io.File
import java.util.zip.ZipInputStream

interface ArchiveExtractor {
  fun extract(archive: File, destination: File)
}

class ZipArchiveExtractor : ArchiveExtractor {
  override fun extract(archive: File, destination: File) {
    ZipInputStream(archive.inputStream()).use { zip ->
      generateSequence { zip.nextEntry }.forEach { entry ->
        val outputFile = File(destination, entry.name)

        if (entry.isDirectory) {
          outputFile.mkdirs()
        } else {
          outputFile.parentFile.mkdirs()
          outputFile.outputStream().use { zip.copyTo(it) }
        }
      }
    }
  }
}