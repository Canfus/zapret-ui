package domain.repository

import domain.model.ZapretConfig
import domain.model.ZapretStatus

interface ZapretRepository {
  suspend fun isInstalled(): Boolean
  suspend fun install()
  suspend fun start(config: ZapretConfig)
  suspend fun stop()
  suspend fun status(): ZapretStatus
}