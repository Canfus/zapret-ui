package ru.zapret.infrastructure.path

import java.io.File

import ru.zapret.config.ZapretRuntimeConfig

class ZapretPaths(
  val zapretDir: File,
  val serviceBat: File
) {
  companion object {
    fun fromAppData(config: ZapretRuntimeConfig): ZapretPaths {
      val appdata = System.getenv("LOCALAPPDATA") ?: error("LOCALAPPDATA not found")

      val zapret = File(appdata, config.installDirName)
      val service = File(zapret, config.serviceBatName)

      return ZapretPaths(
        zapretDir = zapret,
        serviceBat = service
      )
    }
  }
}