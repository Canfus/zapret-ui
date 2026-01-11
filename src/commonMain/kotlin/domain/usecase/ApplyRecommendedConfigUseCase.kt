package domain.usecase

import domain.model.ZapretConfig
import domain.repository.ZapretRepository

class ApplyRecommendedConfigUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(config: ZapretConfig) {
    repository.applyRecommendedConfig(config)
  }
}