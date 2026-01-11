package features.zapret.usecase

import core.data.PathProvider
import features.zapret.data.FlagManager
import features.zapret.data.ZapretRepositoryImpl
import features.zapret.domain.ZapretFlag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BootstrapZapretUseCase(
  private val paths: PathProvider,
  private val repository: ZapretRepositoryImpl,
  private val flagManager: FlagManager
) {
  operator fun invoke(): Flow<BootstrapState> = flow {
    emit(BootstrapState.Checking)

    if (!paths.isZapretInstalled()) {
      emit(BootstrapState.Downloading("Скачивание новой версии zapret..."))

      try {
        val release = repository.getLatestRelease()
        val response = repository.download(release.zipUrl)

        repository.extract(response)

        emit(BootstrapState.Ready)
      } catch (e: Exception) {
        emit(BootstrapState.Error("Не удалось загрузить zapret: ${e.message}"))
      }
    } else {
      if (flagManager.isEnabled(ZapretFlag.AUTO_UPDATES)) {
        TODO("Реализовать автообновление zapret")
      }

      if (flagManager.isEnabled(ZapretFlag.AUTO_TEST_ON_LOAD)) {
        emit(BootstrapState.RunningAutoTests)
      }

      emit(BootstrapState.Ready)
    }
  }
}

sealed class BootstrapState {
  data object Checking : BootstrapState()
  data class Downloading(val message: String) : BootstrapState()
  data object RunningAutoTests : BootstrapState()
  data object Ready : BootstrapState()
  data class Error(val message: String) : BootstrapState()
}