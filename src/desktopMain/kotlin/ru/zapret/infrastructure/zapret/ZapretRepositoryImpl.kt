package ru.zapret.infrastructure.zapret

import domain.model.ZapretConfig
import domain.model.ZapretStatus
import domain.repository.ZapretRepository
import ru.zapret.config.ZapretRuntimeConfig

import ru.zapret.infrastructure.path.ZapretPaths
import ru.zapret.infrastructure.process.ProcessRunner
import ru.zapret.infrastructure.zapret.install.ZapretInstaller

class ZapretRepositoryImpl(
  private val processRunner: ProcessRunner,
  private val paths: ZapretPaths,
  private val installer: ZapretInstaller,
  private val runtimeConfig: ZapretRuntimeConfig
) : ZapretRepository {
  override suspend fun isInstalled(): Boolean {
    return paths.zapretDir.exists() && paths.serviceBat.exists()
  }

  override suspend fun install() {
    installer.installLatest(paths.zapretDir)
  }

  override suspend fun start(config: ZapretConfig) {
    installConfigAsService(config)

    val startCommand = listOf(
      "cmd.exe",
      "/c",
      paths.serviceBat.absolutePath,
      "start"
    )
    processRunner.start(startCommand, paths.zapretDir)
  }

  override suspend fun stop() {
    processRunner.stop(runtimeConfig.processName)
  }

  override suspend fun status(): ZapretStatus {
    return if (processRunner.isRunning(runtimeConfig.processName)) {
      ZapretStatus.RUNNING
    } else {
      ZapretStatus.STOPPED
    }
  }

  override suspend fun listConfigs(): List<ZapretConfig> {
    if (!paths.zapretDir.exists()) return emptyList()

    return paths.zapretDir
      .listFiles { file ->
        file.isFile &&
                file.name.endsWith(".bat", true) &&
                !file.name.startsWith("service", true)
      }
      ?.sortedBy { it.name.lowercase() }
      ?.map { file ->
        ZapretConfig(
          configName = file.nameWithoutExtension,
          enableGameFilter = false,
          autoTests = false
        )
      }.orEmpty()
  }

  override suspend fun getActiveConfig(): ZapretConfig? {
    val command = listOf(
      "reg",
      "query",
      "HKLM\\System\\CurrentControlSet\\Services\\zapret",
      "/v",
      "zapret-discord-youtube"
    )

    processRunner.start()
  }
}