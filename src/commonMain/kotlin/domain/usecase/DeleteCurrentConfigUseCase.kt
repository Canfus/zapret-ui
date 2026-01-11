package domain.usecase

import domain.repository.ZapretRepository

class DeleteCurrentConfigUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke() {
    repository.deleteCurrentConfig()
  }
}