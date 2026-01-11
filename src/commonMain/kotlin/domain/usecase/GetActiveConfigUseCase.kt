package domain.usecase

import domain.model.ZapretConfig
import domain.repository.ZapretRepository

class GetActiveConfigUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): ZapretConfig? {
    return repository.getActiveConfig()
  }
}