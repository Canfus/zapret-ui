package domain.usecase

import domain.model.ZapretStatus
import domain.repository.ZapretRepository

class GetZapretStatusUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): ZapretStatus {
    return repository.status()
  }
}