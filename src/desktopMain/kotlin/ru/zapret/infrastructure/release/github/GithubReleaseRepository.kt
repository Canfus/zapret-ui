package ru.zapret.infrastructure.release.github

import domain.model.ReleaseInfo
import domain.repository.ReleaseRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.zapret.config.ZapretRuntimeConfig

class GithubReleaseRepository(
  private val httpClient: HttpClient,
  private val runtimeConfig: ZapretRuntimeConfig
) : ReleaseRepository {
  override suspend fun getLatest(): ReleaseInfo {
    val dto: GithubReleaseDto = httpClient
      .get(runtimeConfig.githubDownloadUrl)
      .body()

    val asset = dto.assets.firstOrNull {
      it.name.contains("win", true)
    } ?: error("No windows asset found in zapret release")

    return ReleaseInfo(
      version = dto.tagName,
      downloadUrl = asset.browserDownloadUrl
    )
  }
}