package di

import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import core.data.*
import presentation.MainViewModel
import features.zapret.data.FlagManager
import features.zapret.data.StrategyManager
import features.zapret.data.WindowsServiceRepositoryImpl
import features.zapret.data.ZapretRepositoryImpl
import features.zapret.domain.AutoTestAnalyzer
import features.zapret.domain.ZapretStateService
import features.zapret.usecase.BootstrapZapretUseCase
import features.zapret.usecase.RunAutoTestsUseCase
import features.zapret.usecase.RunZapretCommandUseCase

val appModule = module {
  single { ConfigLoader() }
  single { AutoTestAnalyzer() }
  single { ProcessExecutor() }
  
  single {
    HttpClient(CIO) {
      install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
      }
    }
  }

  single { PathProvider(get()) }
  single { WindowsServiceRepositoryImpl(get()) }
  single { ZapretStateService(get(), get()) }
  single { FlagManager(get()) }
  single { StrategyManager(get()) }
  single { ZapretRepositoryImpl(get(), get(), get()) }
  single { RunZapretCommandUseCase(get()) }
  single { MainViewModel(get(), get()) }
  single { RunAutoTestsUseCase(get(), get()) }

  factory { BootstrapZapretUseCase(get(), get(), get()) }
}
