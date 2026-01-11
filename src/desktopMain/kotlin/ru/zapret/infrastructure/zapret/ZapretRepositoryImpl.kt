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
    val command = buildCommand(config)
    processRunner.start(command, paths.zapretDir)
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

  private fun buildCommand(config: ZapretConfig): List<String> {
    return listOf(
      paths.serviceBat.absolutePath,
      "--config=${config.configName}",
      if (config.enableGameFilter) "--game" else "",
      if (config.autoTests) "--autotest" else ""
    ).filter { it.isNotBlank() }
  }
}