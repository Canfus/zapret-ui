package domain.usecase

import domain.model.ZapretConfig
import domain.repository.ZapretRepository

class InstallConfigUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(config: ZapretConfig) {
    return repository.installConfigAsService(config)
  }
}