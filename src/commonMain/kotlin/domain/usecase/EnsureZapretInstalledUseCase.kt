package domain.usecase

import domain.repository.ZapretRepository

class EnsureZapretInstalledUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke() {
    if (!repository.isInstalled()) {
      repository.install()
    }
  }
}