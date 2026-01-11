package domain.repository

import domain.model.AutoTestResult
import domain.model.IpsetStatus
import domain.model.ZapretConfig
import domain.model.ZapretStatus

interface ZapretRepository {
  suspend fun isInstalled(): Boolean
  suspend fun install()
  suspend fun start(config: ZapretConfig)
  suspend fun stop()
  suspend fun status(): ZapretStatus
  suspend fun listConfigs(): List<ZapretConfig>
  suspend fun getActiveConfig(): ZapretConfig?
  suspend fun installConfigAsService(config: ZapretConfig)
  suspend fun deleteCurrentConfig()
  suspend fun checkServiceStatus()
  suspend fun runDiagnostics()
  suspend fun getGameFilterState(): Boolean
  suspend fun setGameFilterState(value: Boolean)
  suspend fun getCheckUpdatesState(): Boolean
  suspend fun setCheckUpdatesState(value: Boolean)
  suspend fun getAutoTestOnLoadState(): Boolean
  suspend fun setAutoTestOnLoadState(value: Boolean)
  suspend fun getIpsetStatus(): IpsetStatus
  suspend fun runAutoTests(): AutoTestResult
  suspend fun applyRecommendedConfig(config: ZapretConfig)
}