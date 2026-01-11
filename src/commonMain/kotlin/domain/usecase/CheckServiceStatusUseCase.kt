package domain.usecase

import domain.repository.ZapretRepository

class CheckServiceStatusUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke() {
    repository.checkServiceStatus()
  }
}