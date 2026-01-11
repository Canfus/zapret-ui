package features.zapret.usecase

import features.zapret.data.ZapretRepositoryImpl
import features.zapret.domain.ZapretCommand
import kotlinx.coroutines.flow.Flow

class RunZapretCommandUseCase(
  private val repository: ZapretRepositoryImpl
) {
  operator fun invoke(command: ZapretCommand): Flow<String> {
    return repository.runServiceCommand(listOf(command.id))
  }
}