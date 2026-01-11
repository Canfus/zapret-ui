package di

import org.koin.dsl.module

import core.data.*
import features.zapret.data.FlagManager
import features.zapret.data.StrategyManager
import features.zapret.data.ZapretRepositoryImpl
import features.zapret.domain.AutoTestAnalyzer
import features.zapret.usecase.BootstrapZapretUseCase
import features.zapret.usecase.RunZapretCommandUseCase

val appModule = module {
  single { ConfigLoader() }
  single { AutoTestAnalyzer() }
  single { ProcessExecutor() }
  
  single { PathProvider(get()) }
  single { FlagManager(get()) }
  single { StrategyManager(get()) }
  single { ZapretRepositoryImpl(get(), get()) }
  single { RunZapretCommandUseCase(get()) }

  factory { BootstrapZapretUseCase(get(), get(), get()) }
}
