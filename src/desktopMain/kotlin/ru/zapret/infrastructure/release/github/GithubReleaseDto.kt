package ru.zapret.infrastructure.release.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubReleaseDto(
  @SerialName("tag_name")
  val tagName: String,
  val assets: List<GithubAssetDto>
)

@Serializable
data class GithubAssetDto(
  val name: String,
  @SerialName("browser_download_url")
  val browserDownloadUrl: String
)