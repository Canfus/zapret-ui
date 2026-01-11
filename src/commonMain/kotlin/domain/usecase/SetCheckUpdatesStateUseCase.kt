package domain.usecase

import domain.repository.ZapretRepository

class SetCheckUpdatesStateUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(value: Boolean) {
    repository.setCheckUpdatesState(value)
  }
}