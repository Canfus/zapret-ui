package domain.usecase

import domain.model.AutoTestResult
import domain.repository.ZapretRepository

class RunAutoTestsUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): AutoTestResult {
    return repository.runAutoTests()
  }
}