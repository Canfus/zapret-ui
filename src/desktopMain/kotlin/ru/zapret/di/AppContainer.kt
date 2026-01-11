package ru.zapret.di

import domain.repository.ReleaseRepository
import domain.repository.ZapretRepository
import domain.usecase.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import ru.zapret.config.RuntimeConfigLoader
import ru.zapret.config.ZapretRuntimeConfig
import ru.zapret.infrastructure.archive.ArchiveExtractor
import ru.zapret.infrastructure.archive.ZipArchiveExtractor
import ru.zapret.infrastructure.download.FileDownloader
import ru.zapret.infrastructure.download.HttpFileDownloader
import ru.zapret.infrastructure.path.ZapretPaths
import ru.zapret.infrastructure.process.ProcessRunner
import ru.zapret.infrastructure.process.WindowsProcessRunner
import ru.zapret.infrastructure.release.github.GithubReleaseRepository
import ru.zapret.infrastructure.zapret.ZapretRepositoryImpl
import ru.zapret.infrastructure.zapret.install.ZapretInstaller
import ru.zapret.infrastructure.zapret.install.ZapretInstallerImpl
import ru.zapret.viewmodel.ZapretViewModel

class AppContainer {
  // ---------- Config ----------
  private val runtimeConfig: ZapretRuntimeConfig = RuntimeConfigLoader.load()
  // ----------- HTTP -----------
  private val httpClient: HttpClient = HttpClient(CIO)
  // ------ Infrastructure ------
  val processRunner: ProcessRunner = WindowsProcessRunner()
  val fileDownloader: FileDownloader = HttpFileDownloader(httpClient)
  val archiveExtractor: ArchiveExtractor = ZipArchiveExtractor()
  val releaseRepository: ReleaseRepository = GithubReleaseRepository(httpClient, runtimeConfig)
  val zapretInstaller: ZapretInstaller = ZapretInstallerImpl(
    releaseRepository,
    fileDownloader,
    archiveExtractor
  )
  val zapretPaths: ZapretPaths = ZapretPaths.fromAppData(runtimeConfig)
  val zapretRepository: ZapretRepository = ZapretRepositoryImpl(
    processRunner = processRunner,
    paths = zapretPaths,
    installer = zapretInstaller,
    runtimeConfig = runtimeConfig
  )
  // --------- Use cases --------
  val ensureZapretInstalled = EnsureZapretInstalledUseCase(zapretRepository)
  val startZapret = StartZapretUseCase(zapretRepository)
  val stopZapret = StopZapretUseCase(zapretRepository)
  val getZapretStatus = GetZapretStatusUseCase(zapretRepository)

  val zapretViewModel = ZapretViewModel(
    ensureInstalled = ensureZapretInstalled,
    startZapret = startZapret,
    stopZapret = stopZapret,
    getStatus = getZapretStatus,
    processRunner = processRunner
  )
}