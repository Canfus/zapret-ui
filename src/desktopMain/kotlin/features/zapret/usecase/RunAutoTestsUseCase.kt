package features.zapret.usecase

import features.zapret.data.ZapretRepositoryImpl
import features.zapret.domain.AutoTestAnalyzer
import features.zapret.domain.ZapretCommand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunAutoTestsUseCase(
  private val repository: ZapretRepositoryImpl,
  private val analyzer: AutoTestAnalyzer
) {
  operator fun invoke(): Flow<AutoTestResult> = flow {
    emit(AutoTestResult.Started)

    val collectedLogs = mutableListOf<String>()

    repository.runServiceCommand(listOf(ZapretCommand.RunAutoTests.id, "1", "1")).collect { line ->
      collectedLogs.add(line)
      emit(AutoTestResult.LogLine(line))
    }

    val bestConfig = analyzer.findBestConfig(collectedLogs)

    if (bestConfig != null) {
      emit(AutoTestResult.Success(bestConfig))
    } else {
      emit(AutoTestResult.Error("Оптимальный конфиг не найден"))
    }
  }
}

sealed class AutoTestResult {
    data object Started : AutoTestResult()
    data class LogLine(val text: String) : AutoTestResult()
    data class Success(val bestConfig: String) : AutoTestResult()
    data class Error(val message: String) : AutoTestResult()
}