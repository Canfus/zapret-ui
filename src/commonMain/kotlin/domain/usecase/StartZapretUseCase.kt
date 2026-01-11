package domain.usecase

import domain.model.ZapretConfig
import domain.repository.ZapretRepository

class StartZapretUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(config: ZapretConfig) {
    repository.start(config)
  }
}