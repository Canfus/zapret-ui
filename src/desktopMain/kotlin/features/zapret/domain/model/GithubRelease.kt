package features.zapret.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
  @SerialName("tag_name") val tagName: String,
  @SerialName("zipball_url") val zipUrl: String,
  @SerialName("html_url") val htmlUrl: String
)