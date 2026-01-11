package domain.usecase

import domain.model.ZapretConfig
import domain.repository.ZapretRepository

class GetConfigsUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): List<ZapretConfig> {
    return repository.listConfigs()
  }
}