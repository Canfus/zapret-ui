package domain.usecase

import domain.repository.ZapretRepository

class GetAutoTestOnLoadStateUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): Boolean {
    return repository.getAutoTestOnLoadState()
  }
}