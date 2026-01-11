package domain.usecase

import domain.repository.ZapretRepository

class SetGameFilterStateUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(value: Boolean) {
    repository.setGameFilterState(value)
  }
}