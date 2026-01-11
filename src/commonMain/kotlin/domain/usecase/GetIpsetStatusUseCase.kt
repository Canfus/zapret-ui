package domain.usecase

import domain.model.IpsetStatus
import domain.repository.ZapretRepository

class GetIpsetStatusUseCase(
  private val repository: ZapretRepository
) {
  suspend operator fun invoke(): IpsetStatus {
    return repository.getIpsetStatus()
  }
}