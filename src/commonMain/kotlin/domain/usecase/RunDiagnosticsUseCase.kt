package domain.usecase

import domain.repository.ZapretRepository

class RunDiagnosticsUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke() {
    repository.runDiagnostics()
  }
}