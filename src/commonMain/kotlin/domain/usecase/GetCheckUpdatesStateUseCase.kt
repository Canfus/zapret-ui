package domain.usecase

import domain.repository.ZapretRepository

class GetCheckUpdatesStateUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): Boolean {
    return repository.getCheckUpdatesState()
  }
}