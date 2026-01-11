package domain.usecase

import domain.repository.ZapretRepository

class SetAutoTestOnLoadStateUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(value: Boolean) {
    repository.setAutoTestOnLoadState(value)
  }
}