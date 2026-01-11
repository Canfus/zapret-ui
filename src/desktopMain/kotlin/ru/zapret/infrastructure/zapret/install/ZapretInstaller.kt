package ru.zapret.infrastructure.zapret.install

import java.io.File
import domain.repository.ReleaseRepository

import ru.zapret.infrastructure.download.FileDownloader
import ru.zapret.infrastructure.archive.ArchiveExtractor

interface ZapretInstaller {
  suspend fun installLatest(targetDir: File)
}

class ZapretInstallerImpl(
  private val releaseRepository: ReleaseRepository,
  private val downloader: FileDownloader,
  private val extractor: ArchiveExtractor
) : ZapretInstaller {
  override suspend fun installLatest(targetDir: File) {
    val release = releaseRepository.getLatest()

    val archive = File.createTempFile("zapret-${release.version}", ".zip")

    downloader.download(release.downloadUrl, archive)
    extractor.extract(archive, targetDir)

    archive.delete()
  }
}