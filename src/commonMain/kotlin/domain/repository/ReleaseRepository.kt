package domain.repository

import domain.model.ReleaseInfo

interface ReleaseRepository {
  suspend fun getLatest(): ReleaseInfo
}